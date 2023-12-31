package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Submit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmitRepository extends JpaRepository<Submit, Long> {
    List<Submit> findAllByProblem(Problem problem);

    List<Submit> findAllByMemberAndProblem(Member member, Problem problem);

    Long countByMemberAndProblem(Member member, Problem problem);

    List<Submit> findAllByMemberAndProblemOrderByCreatedAtDesc(Member member, Problem problem);

}
