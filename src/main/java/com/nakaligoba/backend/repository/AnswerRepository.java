package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Answer;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Long countByMemberAndProblem(Member member, Problem problem);

    List<Answer> findByMemberAndProblem(Member member, Problem problem);
}
