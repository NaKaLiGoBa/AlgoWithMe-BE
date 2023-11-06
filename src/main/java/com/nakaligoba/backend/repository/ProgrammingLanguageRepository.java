package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.ProgrammingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgrammingLanguageRepository extends JpaRepository<ProgrammingLanguage, Long> {
    Optional<ProgrammingLanguage> findByName(String name);
}
