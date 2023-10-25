package com.nakaligoba.backend.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.nakaligoba.backend.domain.ProjectFile;
import com.nakaligoba.backend.entity.FileEntity;
import com.nakaligoba.backend.entity.ProjectEntity;
import com.nakaligoba.backend.entity.mapper.Mapper;
import com.nakaligoba.backend.repository.FileRepository;
import com.nakaligoba.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunFileService {

    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;
    private final S3Client s3Client;
    private final DockerClient dockerClient;
    private final Mapper<ProjectFile, FileEntity> fileMapper;

    private static final String CODE_STORAGE_PATH = "/app/data/";
    private static final String BUCKET_NAME = "web-ide-projects";

    @Transactional
    public String run(Long projectId, Long fileId) {
        FileEntity fileEntity = getFileEntity(projectId, fileId);
        log.info(fileEntity.toString());
        String key = fileEntity.getStorageFileId();
        ProjectFile projectFile = fileMapper.entityToDomain(fileEntity);
        clearVolume();
        loadFileFromS3toVolume(key);
        return runFile(projectFile);
    }

    private FileEntity getFileEntity(Long projectId, Long fileId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(NoSuchElementException::new);
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(NoSuchElementException::new);

        if (!file.getProject().equals(project)) {
            throw new IllegalArgumentException("프로젝트에 해당 파일이 존재하지 않습니다.");
        }

        return file;
    }

    private void clearVolume() {
        File volume = new File(CODE_STORAGE_PATH);
        deleteAllFiles(volume);
        log.info("Volume is cleared");
    }

    private void deleteAllFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteAllFiles(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }

    private void loadFileFromS3toVolume(String key) {
        String filename = getFilename(key);
        log.info("Loaded file: {}", filename);
        Path destination = Paths.get(CODE_STORAGE_PATH + filename);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.getObject(getObjectRequest, ResponseTransformer.toFile(destination));
    }

    private String getFilename(String key) {
        int i = key.lastIndexOf("/");
        return key.substring(i + 1);
    }

    private String runFile(ProjectFile file) {
        final String shell = "/bin/sh";
        final String option = "-c";

        log.info("Target file: {}", file.getName());

        String runner = file.getRunner();
        String command = file.getCommand();

        log.info("run: {}", command);

        ExecCreateCmdResponse response = dockerClient.execCreateCmd(runner)
                .withAttachStdout(true)
                .withCmd(shell, option, command)
                .exec();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            dockerClient.execStartCmd(response.getId())
                    .exec(new ExecStartResultCallback(outputStream, null))
                    .awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toString();
    }
}
