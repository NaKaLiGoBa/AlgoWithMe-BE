package com.nakaligoba.backend.programminglanguage.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgrammingLanguageRepository extends JpaRepository<ProgrammingLanguage, Long> {
    Optional<ProgrammingLanguage> findByName(String name);
}
