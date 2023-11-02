package com.nakaligoba.backend.problem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select max(p.number) from Problem p")
    Optional<Integer> findMaxNumber();
}
