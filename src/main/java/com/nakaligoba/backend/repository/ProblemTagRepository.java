package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.ProblemTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemTagRepository extends JpaRepository<ProblemTag, Long> {
}
