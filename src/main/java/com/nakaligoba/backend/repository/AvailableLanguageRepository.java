package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.AvailableLanguage;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvailableLanguageRepository extends JpaRepository<AvailableLanguage, Long> {
    Optional<AvailableLanguage> findByProgrammingLanguage(ProgrammingLanguage programmingLanguage);
}
