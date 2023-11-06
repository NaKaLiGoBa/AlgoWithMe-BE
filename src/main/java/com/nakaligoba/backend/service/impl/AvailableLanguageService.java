package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.AvailableLanguage;
import com.nakaligoba.backend.repository.AvailableLanguageRepository;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AvailableLanguageService {

    private final AvailableLanguageRepository availableLanguageRepository;

    @Transactional
    public void avail(Problem problem, ProgrammingLanguage programmingLanguage) {
        AvailableLanguage availableLanguage = new AvailableLanguage(problem, programmingLanguage);
        availableLanguageRepository.save(availableLanguage);
    }
}
