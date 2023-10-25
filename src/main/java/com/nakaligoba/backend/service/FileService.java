package com.nakaligoba.backend.service;

import com.nakaligoba.backend.domain.ProjectFile;
import com.nakaligoba.backend.entity.FileEntity;
import com.nakaligoba.backend.entity.MemberEntity;
import com.nakaligoba.backend.entity.ProjectEntity;
import com.nakaligoba.backend.entity.mapper.Mapper;
import com.nakaligoba.backend.repository.FileRepository;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.repository.ProjectRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final S3Client s3Client;
    private final Mapper<ProjectFile, FileEntity> fileMapper;
    private final String BUCKET_NAME = "web-ide-projects";

    @Transactional
    public Long createFile(CreateFileDto dto) {
        ProjectEntity projectEntity = projectRepository.findById(dto.projectId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 프로젝트를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findByEmail(dto.userEmail);
        String memberId = String.valueOf(member.getId());
        String key = String.format("%s/%s/%s.%s", memberId, projectEntity.getName(), dto.fileName, dto.ext);

        if (fileRepository.existsByStorageFileId(key)) {
            throw new IllegalArgumentException("파일이름이 중복되었습니다.");
        }

        saveFileToS3(key);
        FileEntity fileEntity = saveFileToRepository(projectEntity, key);

        return fileEntity.getId();
    }

    private void saveFileToS3(String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.empty());
    }

    private FileEntity saveFileToRepository(ProjectEntity projectEntity, String key) {
        FileEntity createdFile = FileEntity.builder()
                .storageFileId(key)
                .project(projectEntity)
                .build();
        return fileRepository.save(createdFile);
    }

    @Transactional
    public void updateFile(Long projectId, Long fileId, FileDto fileDto) {
        List<FileEntity> files = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트를 찾을 수 없습니다."))
                .getFiles();

        FileEntity fileEntity = files.stream()
                .filter(file -> Objects.equals(file.getId(), fileId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 파일을 찾을 수 없습니다."));

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fileEntity.getStorageFileId())
                        .build(),
                RequestBody.fromString(fileDto.content));
        log.info("File.storageId: {}", fileEntity.getStorageFileId());
    }

    @Transactional
    public FileDto readFile(Long projectId, Long fileId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트를 찾을 수 없습니다."));
        List<FileEntity> files = projectEntity.getFiles();
        FileEntity fileEntity = files.stream()
                .filter(file -> Objects.equals(file.getId(), fileId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 파일을 찾을 수 없습니다."));

        String key = fileEntity.getStorageFileId();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        log.info("Read File: {}", key);

        byte[] objectBytes = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes())
                .asByteArray();

        ProjectFile projectFile = fileMapper.entityToDomain(fileEntity);

        return FileDto.builder()
                .fileName(projectFile.getName())
                .ext(projectFile.getLanguage().getExt())
                .content(new String(objectBytes, StandardCharsets.UTF_8))
                .build();
    }

    @Transactional
    public void deleteFile(Long projectId, Long fileId) {
        List<FileEntity> files = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당 프로젝트를 찾을 수 없습니다."))
                .getFiles();

        FileEntity fileEntity = files.stream()
                .filter(file -> Objects.equals(file.getId(), fileId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 파일을 찾을 수 없습니다."));
        fileRepository.delete(fileEntity);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileEntity.getStorageFileId())
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileDto {
        private String fileName;
        private String ext;
        private String content;
    }

    @Data
    @Builder
    public static class CreateFileDto {
        private final String fileName;
        private final String ext;
        private final String userEmail;
        private final Long projectId;
    }
}
