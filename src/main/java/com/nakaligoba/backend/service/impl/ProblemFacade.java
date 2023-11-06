package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import com.nakaligoba.backend.domain.Tag;
import com.nakaligoba.backend.domain.Testcase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemFacade {

    private final ProblemService problemService;
    private final TestcaseService testcaseService;
    private final TagService tagService;
    private final ProblemTagService problemTagService;
    private final ProgrammingLanguageService programmingLanguageService;
    private final AvailableLanguageService availableLanguageService;

    @Transactional
    public Long createProblem(CreateProblemRequest dto) {
        Problem problem = problemService.createProblem(dto.getTitle(), dto.getDescription(), dto.getDifficulty());
        setTestcases(dto.getTestcases(), dto.getParameters(), problem);
        setAnswerCases(dto.getAnswerCases(), dto.getParameters(), problem);
        setTags(dto.getTags(), problem);
        setProgrammingLanguages(problem);
        return problem.getId();
    }

    private void setTestcases(String testcases, List<String> parameters, Problem problem) {
        String[] testInputsAndOutputs = testcases.split("\n");
        for (int i = 0; i < testInputsAndOutputs.length; i += 2) {
            String inputs = testInputsAndOutputs[i];
            String output = testInputsAndOutputs[i + 1];
            int number = i / 2 + 1;
            Testcase testcase = testcaseService.createTestcase(number, parameters, inputs, output, problem);
            problem.addTestcase(testcase);
        }
    }

    private void setAnswerCases(String answerCases, List<String> parameters, Problem problem) {
        String[] answerInputsAndOutputs = answerCases.split("\n");
        for (int i = 0; i < answerInputsAndOutputs.length; i += 2) {
            String inputs = answerInputsAndOutputs[i];
            String output = answerInputsAndOutputs[i + 1];
            Testcase answerCase = testcaseService.createAnswerCase(parameters, inputs, output, problem);
            problem.addTestcase(answerCase);
        }
    }

    private void setTags(List<String> tags, Problem problem) {
        for (String name : tags) {
            Tag tag = tagService.findOrCreateTag(name);
            problem.addProblemTag(tag);
            problemTagService.tag(problem, tag);
        }
    }

    private void setProgrammingLanguages(Problem problem) {
        List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.findAll();
        programmingLanguages.forEach(problem::addAvailableLanguage);
        programmingLanguages.forEach(p -> availableLanguageService.avail(problem, p));
    }

}
