package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Solution;
import com.nakaligoba.backend.domain.SolutionLike;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.SolutionLikeRepository;
import com.nakaligoba.backend.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionLikeService {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionLikeRepository solutionLikeRepository;

    @Transactional
    public boolean toggleLike(String email, Long problemId, Long solutionId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(EntityNotFoundException::new);

        Optional<SolutionLike> optionalSolutionLike = solutionLikeRepository.findByMemberAndSolution(member, solution);

        if (optionalSolutionLike.isEmpty()) {
            SolutionLike like = new SolutionLike(member, solution);
            solutionLikeRepository.save(like);
            return true;
        }

        SolutionLike solutionLike = optionalSolutionLike.get();
        solutionLikeRepository.delete(solutionLike);
        return false;
    }
}
