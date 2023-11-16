package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
