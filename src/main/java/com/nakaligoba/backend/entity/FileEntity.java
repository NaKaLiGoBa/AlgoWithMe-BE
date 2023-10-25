package com.nakaligoba.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_file_id", nullable = false)
    private String storageFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Builder
    public FileEntity(String storageFileId, ProjectEntity project) {
        this.storageFileId = storageFileId;
        this.project = project;
    }

}
