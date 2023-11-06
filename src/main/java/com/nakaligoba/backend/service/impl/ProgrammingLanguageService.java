package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.ProgrammingLanguage;
import com.nakaligoba.backend.repository.ProgrammingLanguageRepository;
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
