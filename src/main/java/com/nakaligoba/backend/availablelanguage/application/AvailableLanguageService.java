package com.nakaligoba.backend.availablelanguage.application;

import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguage;
import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguageRepository;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
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
