package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select max(p.number) from Problem p")
    Optional<Integer> findMaxNumber();

    @Query("SELECT p FROM Problem p LEFT JOIN p.submits s LEFT JOIN p.problemTags pt LEFT JOIN pt.tag t " +
            "WHERE (:status IS NULL OR s.result = :status) " +
            "AND (:difficulty IS NULL OR p.difficulty = :difficulty) " +
            "AND (:tags IS EMPTY OR t.name IN :tags)")
    Page<Problem> findAllWithFilters(Pageable pageable,
                                     @Param("status") Result status,
                                     @Param("difficulty") Difficulty difficulty,
                                     @Param("tags") List<String> tags);
}
