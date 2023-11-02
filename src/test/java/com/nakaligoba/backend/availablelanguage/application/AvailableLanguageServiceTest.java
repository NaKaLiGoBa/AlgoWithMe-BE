package com.nakaligoba.backend.availablelanguage.application;

import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguage;
import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguageRepository;
import com.nakaligoba.backend.problem.domain.Difficulty;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.domain.ProblemRepository;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AvailableLanguageServiceTest {

    @Autowired
    AvailableLanguageService availableLanguageService;

    @Autowired
    AvailableLanguageRepository availableLanguageRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    ProgrammingLanguageRepository programmingLanguageRepository;

    @DisplayName("문제에 사용 가능한 언어를 설정할 수 있다.")
    @Test
    @Transactional
    void test() {
        Problem problem = Problem.defaultBuilder()
                .number(1)
                .title("제목")
                .description("설명")
                .difficulty(Difficulty.HARD)
                .build();
        ProgrammingLanguage language = new ProgrammingLanguage("Java");
        problemRepository.save(problem);
        programmingLanguageRepository.save(language);

        availableLanguageService.avail(problem, language);

        AvailableLanguage availableLanguage = availableLanguageRepository.findAll().get(0);

        assertThat(availableLanguage.getProgrammingLanguage()).isEqualTo(language);
        assertThat(availableLanguage.getProblem()).isEqualTo(problem);
    }
}
