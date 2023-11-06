package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select max(p.number) from Problem p")
    Optional<Integer> findMaxNumber();
}
