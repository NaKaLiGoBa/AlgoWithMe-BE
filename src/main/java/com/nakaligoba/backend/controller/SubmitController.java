package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.response.SubmitCodeResponse;
import com.nakaligoba.backend.controller.payload.response.SubmitsResponse;
import com.nakaligoba.backend.service.dto.SubmitDto;
import com.nakaligoba.backend.service.impl.SubmitService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Slf4j
@RequestMapping("/api/v1/problems/{problemId}/submits")
@RequiredArgsConstructor
public class SubmitController {

    private final SubmitService submitService;

    @GetMapping
    public ResponseEntity<SubmitsResponse> getAllSubmits(@PathVariable Long problemId) {
        String email = JwtUtils.getEmailFromSpringSession();
        Collection<SubmitDto> submits = submitService.readAll(email, problemId);
        SubmitsResponse response = new SubmitsResponse(submits.size(), submits);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{submitId}")
    public ResponseEntity<SubmitCodeResponse> getSubmitCode(@PathVariable Long problemId, @PathVariable Long submitId) {
        String submitCode = submitService.getSubmitCode(submitId);
        SubmitCodeResponse submitCodeResponse = new SubmitCodeResponse(submitCode);

        return ResponseEntity.ok(submitCodeResponse);
    }
}
