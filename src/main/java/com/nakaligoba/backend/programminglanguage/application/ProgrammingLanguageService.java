package com.nakaligoba.backend.programminglanguage.application;

import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProgrammingLanguageService {

    private final ProgrammingLanguageRepository programmingLanguageRepository;

    public List<ProgrammingLanguage> findAll() {
        return programmingLanguageRepository.findAll();
    }
}
