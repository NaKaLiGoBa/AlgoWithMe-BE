package com.nakaligoba.backend.entity.mapper;

import com.nakaligoba.backend.domain.Language;
import com.nakaligoba.backend.domain.ProjectFile;
import com.nakaligoba.backend.entity.FileEntity;
import org.springframework.stereotype.Component;

@Component
public class FileMapper implements Mapper<ProjectFile, FileEntity> {

    @Override
    public ProjectFile entityToDomain(FileEntity entity) {
        String storageId = entity.getStorageFileId();
        return ProjectFile.builder()
                .name(getFilename(storageId))
                .directory(getDirectory(storageId))
                .language(getLanguage(storageId))
                .build();
    }

    @Override
    public FileEntity domainToEntity(ProjectFile domain) {
        return FileEntity.builder()
                .storageFileId(getStorageFileId(domain))
                .build();
    }

    private String getFilename(String storageId) {
        int s = storageId.lastIndexOf("/");
        int e = storageId.lastIndexOf(".");
        return storageId.substring(s + 1, e);
    }

    private String getDirectory(String storageId) {
        int i = storageId.lastIndexOf("/");
        return storageId.substring(0, i);
    }

    private Language getLanguage(String storageId) {
        int i = storageId.lastIndexOf(".");
        String ext = storageId.substring(i + 1);
        return Language.getByExt(ext);
    }

    private static String getStorageFileId(ProjectFile domain) {
        return domain.getDirectory() + domain.getName() + domain.getLanguage().getExt();
    }
}
