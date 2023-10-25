package com.nakaligoba.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
public class ProjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "storage_id", nullable = false)
    private String storageId;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FileEntity> files = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<MemberProjectEntity> collaborators = new ArrayList<>();

    @Builder
    public ProjectEntity(String name, String description, String storageId) {
        this.name = name;
        this.description = description;
        this.storageId = storageId;
    }

    public void changeProjectName(String ProjectName) {
        this.name = ProjectName;
    }
    public void changeDescription(String description) {
        this.description = description;
    }
}
