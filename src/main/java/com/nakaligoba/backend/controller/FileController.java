package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.service.FileService;
import com.nakaligoba.backend.service.RunFileService;
import com.nakaligoba.backend.utils.JwtUtils;
import io.jsonwebtoken.Jwt;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.nakaligoba.backend.service.FileService.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Path;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/projects/{projectId}/files")
public class FileController {

    private final FileService fileService;
    private final RunFileService runFileService;
    private final JwtUtils jwtUtils;

    @GetMapping("/{fileId}")
    public ResponseEntity<FileDto> readFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId) {
        FileDto fileDto = fileService.readFile(projectId, fileId);
        return ResponseEntity.ok(fileDto);
    }

    @PostMapping
    public ResponseEntity<CreateFileResponse> saveFile(
            @PathVariable Long projectId,
            @Valid @RequestBody FileRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();
        String email = jwtUtils.getEmailFromSpringSession();
        String[] split = request.getName().split("\\.");
        CreateFileDto dto = CreateFileDto.builder()
                .fileName(split[0])
                .ext(split[1])
                .userEmail(email)
                .projectId(projectId)
                .build();
        log.info("Logged in Member email: {}", dto.getUserEmail());

        Long fileId = fileService.createFile(dto);
        CreateFileResponse response = new CreateFileResponse(fileId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<Void> updateFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId,
            @Valid @RequestBody FileRequest request
    ) {
        String[] split = request.getName().split("\\.");
        FileDto fileDto = FileDto.builder()
                .fileName(split[0])
                .ext(split[1])
                .content(request.getContent())
                .build();

        fileService.updateFile(projectId, fileId, fileDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileId}/run")
    public ResponseEntity<RunResponse> runFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId
    ) {
        String result = runFileService.run(projectId, fileId);
        RunResponse response = new RunResponse(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long projectId,
            @PathVariable Long fileId
    ) {
        try {
            fileService.deleteFile(projectId, fileId);
            return ResponseEntity.ok("파일이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 삭제 중 오류가 발생했습니다.");
        }
    }

    @Data
    static class FileRequest {
        @NotBlank
        private final String name;
        private final String content;
    }

    @Data
    static class CreateFileResponse {
        private final Long id;
    }

    @Data
    static class RunResponse {
        private final String message;
    }
}
