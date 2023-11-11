package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.repository.SolutionLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionLikeService {

    private final SolutionLikeRepository solutionLikeRepository;
}
