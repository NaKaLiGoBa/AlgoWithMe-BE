package com.nakaligoba.backend.service;

import com.nakaligoba.backend.entity.FileEntity;
import com.nakaligoba.backend.entity.MemberEntity;
import com.nakaligoba.backend.repository.MemberProjectRepository;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.repository.ProjectRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReadProjectService {

    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

    public DirectoryDto readProjectDirectory(Long id, String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        List<FileEntity> files = memberProjectRepository.findByMemberAndProjectId(member, id)
                .getProject()
                .getFiles();

        List<Node> nodes = new ArrayList<>();

        long key = 0L;
        for (FileEntity file : files) {
            Node root = null;
            String storageFileId = file.getStorageFileId();
            String[] folderAndFileNames = storageFileId.split("/");
            for (int i = 2; i < folderAndFileNames.length - 1; i++) {
                String path = getPath(folderAndFileNames, i);
                Node folderNode;
                if (root != null) {
                    folderNode = root.children.stream()
                            .filter(c -> c.path.equals(path))
                            .findAny()
                            .orElse(null);
                } else {
                    folderNode = nodes.stream()
                            .filter(n -> n.path.equals(path))
                            .findAny()
                            .orElse(null);
                }

                if (folderNode != null) {
                    root = folderNode;
                    continue;
                }
                String folder = folderAndFileNames[i];
                folderNode = Node.builder()
                        .key(String.valueOf(key++))
                        .path(path)
                        .title(folder)
                        .type("folder")
                        .children(new ArrayList<>())
                        .build();
                if (root == null) {
                    nodes.add(folderNode);
                }
                if (root != null) {
                    root.children.add(folderNode);
                }
                root = folderNode;
            }

            String fileName = folderAndFileNames[folderAndFileNames.length - 1];
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
            Node fileNode = Node.builder()
                    .id(String.valueOf(file.getId()))
                    .key(String.valueOf(key++))
                    .path(getPath(folderAndFileNames, folderAndFileNames.length - 1))
                    .title(fileName)
                    .content("")
                    .type(ext)
                    .children(new ArrayList<>())
                    .build();

            if (root == null) {
                nodes.add(fileNode);
                continue;
            }

            root.children.add(fileNode);
        }

        return new DirectoryDto(Collections.unmodifiableList(nodes), key);
    }

    private String getPath(String[] split, int i) {
        return Arrays.stream(split, 2, i + 1)
                .collect(Collectors.joining("/"));
    }

    @Transactional
    public String getUsername(String email) {
        return memberRepository.findByEmail(email)
                .getName();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private String id;
        private String key;
        private String title;
        private String path;
        private String type;
        private String content;
        private List<Node> children;
    }

    @Data
    public static class DirectoryDto {
        private final List<Node> nodes;
        private final Long lastKey;
    }
}
