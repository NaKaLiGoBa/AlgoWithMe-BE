package com.nakaligoba.backend.controller;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.nakaligoba.backend.entity.Role;
import com.nakaligoba.backend.service.ProjectService;
import com.nakaligoba.backend.service.ProjectService.CreateProjectDto;
import com.nakaligoba.backend.service.ProjectService.UpdateProjectDto;
import com.nakaligoba.backend.service.ReadProjectService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ReadProjectService readProjectService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request
    ) {
        String email = jwtUtils.getEmailFromSpringSession();
        CreateProjectDto dto = CreateProjectDto.builder()
                .email(email)
                .name(request.name)
                .description(request.description)
                .template(request.template)
                .build();
        ProjectCreateResponse response = projectService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectListResponse>> getAllProjects() {
        String email = jwtUtils.getEmailFromSpringSession();
        List<ProjectListResponse> projects = projectService.getProjectsByEmail(email);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadProjectDirectoryResponse> readProjectDirectory(@PathVariable Long id) {
        String email = jwtUtils.getEmailFromSpringSession();
        ReadProjectService.DirectoryDto dto = readProjectService.readProjectDirectory(id, email);
        String username = readProjectService.getUsername(email);
        List<ReadProjectService.Node> node = dto.getNodes();
        Long lastKey = dto.getLastKey();
        ReadProjectDirectoryResponse readProjectDirectoryResponse = new ReadProjectDirectoryResponse(node, lastKey, username);
        return ResponseEntity.ok(readProjectDirectoryResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectUpdateRequest request
    ) {
        UpdateProjectDto dto = UpdateProjectDto.builder()
                .projectId(id)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        projectService.updateProject(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        String email = jwtUtils.getEmailFromSpringSession();
        projectService.deleteProject(id, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<?> inviteProject(
            @PathVariable Long id,
            @RequestBody InviteRequest request) {
        String inviterEmail = jwtUtils.getEmailFromSpringSession();
        String inviteeEmail = request.getEmail();
        try {
            projectService.inviteMemberToProject(id, inviterEmail, inviteeEmail);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<?> removeProjectMember (
            @PathVariable Long id,
            @PathVariable Long memberId
    ){
        String email = jwtUtils.getEmailFromSpringSession();
        try {
            projectService.removeMemberToProject(id, memberId, email);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Data
    public static class ProjectCreateRequest {
        private String name;
        private String description;
        private String template;
    }

    @Data
    public static class ProjectUpdateRequest {
        private String name;
        private String description;
    }

    @Data
    @Builder
    public static class ProjectCreateResponse {
        private Long id;
    }

    @Data
    @Builder
    public static class ProjectListResponse {
        private Long id;
        private String me;
        private String name;
        private String description;
        private LocalDateTime updatedAt;
        private List<CollaboratorResponse> collaborators;
    }

    @Data
    @Builder
    public static class CollaboratorResponse {
        private Long id;
        private String name;
        private Role role;
    }

    @Data
    @Builder
    public static class InviteRequest {
        private String email;
    }

    @Data
    public static class ReadProjectDirectoryResponse {
        private final List<ReadProjectService.Node> data;
        private final Long lastKey;
        private final String username;
    }
}
