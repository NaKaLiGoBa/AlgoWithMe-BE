package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    boolean existsByStorageFileId(String storageFileId);
}
