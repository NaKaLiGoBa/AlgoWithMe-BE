package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.MiniQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniQuizRepository extends JpaRepository<MiniQuiz, Long> {
}
