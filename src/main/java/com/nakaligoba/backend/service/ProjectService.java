package com.nakaligoba.backend.service;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.nakaligoba.backend.controller.ProjectController.CollaboratorResponse;
import com.nakaligoba.backend.controller.ProjectController.ProjectCreateResponse;
import com.nakaligoba.backend.controller.ProjectController.ProjectListResponse;
import com.nakaligoba.backend.entity.*;
import com.nakaligoba.backend.repository.FileRepository;
import com.nakaligoba.backend.repository.MemberProjectRepository;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.repository.ProjectRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    private final static String BUCKET_NAME = "web-ide-projects";

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final FileRepository fileRepository;
    private final S3Client s3Client;

    @Transactional
    public ProjectCreateResponse create(CreateProjectDto dto) {
        MemberEntity member = memberRepository.findByEmail(dto.email);

        if (isDuplicatedName(member, dto.getName())) {
            throw new IllegalArgumentException("프로젝트명이 중복되었습니다.");
        }

        String key = String.format("%s/%s/", member.getId(), dto.name);
        ProjectEntity createdProject = ProjectEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .storageId(key)
                .build();
        MemberProjectEntity memberProject = MemberProjectEntity.builder()
                .member(member)
                .project(createdProject)
                .role(Role.OWNER)
                .build();

        createdProject = projectRepository.save(createdProject);
        memberProjectRepository.save(memberProject);

        createDefaultFile(dto.template, key, createdProject);

        log.info("Project(id: {}, name: {}) is created", createdProject.getId(), createdProject.getName());
        return ProjectCreateResponse.builder()
                .id(createdProject.getId())
                .build();
    }

    private void createDefaultFile(String template, String key, ProjectEntity createdProject) {
        String ext = getExt(template);

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key + "src/Main." + ext)
                        .build(),
                RequestBody.empty());

        FileEntity file = FileEntity.builder()
                .storageFileId(key + "src/Main." + ext)
                .project(createdProject)
                .build();
        fileRepository.save(file);
        log.info(file.toString());
    }

    private static String getExt(String template) {
        String ext;
        switch (template) {
            case "Java":
                ext = "java";
                break;
            case "Python":
                ext = "py";
                break;
            case "JavaScript":
                ext = "js";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ext;
    }

    private boolean isDuplicatedName(MemberEntity member, String name) {
        return member.getMemberProjects()
                .stream()
                .map(MemberProjectEntity::getProject)
                .map(ProjectEntity::getName)
                .anyMatch(n -> n.equals(name));
    }

    @Transactional
    public List<ProjectListResponse> getProjectsByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        List<MemberProjectEntity> memberProjects = memberProjectRepository.findByMember(member);

        return memberProjects.stream()
                .map(this::memberProjectToResponse)
                .collect(Collectors.toList());
    }

    private ProjectListResponse memberProjectToResponse(MemberProjectEntity memberProject) {
        ProjectEntity project = memberProject.getProject();
        List<CollaboratorResponse> collaborators = getCollaborators(project);
        return ProjectListResponse.builder()
                .id(project.getId())
                .me(memberProject.getMember().getName())
                .name(project.getName())
                .description(project.getDescription())
                .updatedAt(project.getUpdatedAt())
                .collaborators(collaborators)
                .build();
    }

    private List<CollaboratorResponse> getCollaborators(ProjectEntity project) {
        return project.getCollaborators().stream()
                .map(mp -> CollaboratorResponse.builder()
                        .id(mp.getMember().getId())
                        .name(mp.getMember().getName())
                        .role(mp.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProject(Long projectId, String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        MemberProjectEntity memberProject = memberProjectRepository.findByMemberAndProjectId(member, projectId);

        if (memberProject == null) {
            throw new NoSuchElementException("프로젝트를 찾을 수 없습니다.");
        }
        if (memberProject.getRole() != Role.OWNER) {
            throw new UnauthorizedException("프로젝트 삭제 권한이 없습니다.");
        }

        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void updateProject(UpdateProjectDto dto) {
        ProjectEntity project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(IllegalArgumentException::new);

        project.changeProjectName(dto.getName());
        project.changeDescription(dto.getDescription());
        projectRepository.save(project);
    }

    @Transactional
    public void inviteMemberToProject(Long projectId, String inviterEmail, String inviteeEmail) {
        MemberEntity inviter = validateInviteByEmail(inviterEmail);
        MemberEntity invitee = validateInviteByEmail(inviteeEmail);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        Optional<MemberProjectEntity> ownerRelationOpt = memberProjectRepository
                .findByProjectAndMemberAndRole(project, inviter, Role.OWNER);
        if (!ownerRelationOpt.isPresent()) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        boolean isAlreadyMember = project.getCollaborators().stream()
                .anyMatch(mp -> mp.getMember().equals(invitee));
        if (isAlreadyMember) {
            throw new IllegalArgumentException("이미 초대된 사용자입니다.");
        }

        MemberProjectEntity newMemberProject = new MemberProjectEntity(project, invitee, Role.COLLABORATOR);
        memberProjectRepository.save(newMemberProject);
    }

    private MemberEntity validateInviteByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("입력 값을 잘못 입력하였습니다.");
        }
        return member;
    }

    @Transactional
    public void removeMemberToProject(Long projectId, Long memberId, String email) {
        MemberEntity loggedInMember = memberRepository.findByEmail(email);

        MemberProjectEntity loggedInMemberProject = memberProjectRepository
                .findByMemberAndProjectId(loggedInMember, projectId);
        if (loggedInMemberProject == null || !loggedInMemberProject.getRole().equals(Role.OWNER)) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }

        MemberEntity memberToBeRemoved = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        MemberProjectEntity memberProjectToRemove = memberProjectRepository
                .findByMemberAndProjectId(memberToBeRemoved, projectId);
        if (memberProjectToRemove == null) {
            throw new NotFoundException("멤버의 프로젝트 정보를 찾을 수 없습니다.");
        }

        memberProjectToRemove.getProject().getCollaborators().remove(memberProjectToRemove);
        memberProjectRepository.delete(memberProjectToRemove);
    }

    @Data
    @Builder
    public static class CreateProjectDto {
        private final String name;
        private final String description;
        private final String email;
        private final String template;
    }

    @Data
    @Builder
    public static class DeleteProjectDto {
        private final Long projectId;
        private final String userEmail;
    }

    @Data
    @Builder
    public static class UpdateProjectDto {
        private final Long projectId;
        private final String name;
        private final String description;
    }
}
