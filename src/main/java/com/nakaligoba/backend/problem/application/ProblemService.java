package com.nakaligoba.backend.problem.application;

import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguage;
import com.nakaligoba.backend.problem.application.dto.ProblemPagingDto;
import com.nakaligoba.backend.problem.controller.dto.CreateProblemRequest;
import com.nakaligoba.backend.problem.controller.dto.CustomPageResponse;
import com.nakaligoba.backend.problem.controller.dto.InputDto;
import com.nakaligoba.backend.problem.controller.dto.ProblemResponse;
import com.nakaligoba.backend.problem.domain.Difficulty;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.domain.ProblemRepository;
import com.nakaligoba.backend.problemtag.application.ProblemTagService;
import com.nakaligoba.backend.problemtag.domain.ProblemTag;
import com.nakaligoba.backend.programminglanguage.application.ProgrammingLanguageService;
import com.nakaligoba.backend.submit.domain.Result;
import com.nakaligoba.backend.submit.domain.Submit;
import com.nakaligoba.backend.tag.application.TagService;
import com.nakaligoba.backend.tag.domain.Tag;
import com.nakaligoba.backend.testcase.application.TestcaseService;
import com.nakaligoba.backend.testcase.domain.Testcase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    private final ProgrammingLanguageService programmingLanguageService;

    public CustomPageResponse<ProblemPagingDto> getProblemList(Pageable pageable) {
        Page<Problem> page = problemRepository.findAll(pageable);
        List<ProblemPagingDto> dtos = page.getContent().stream()
                .map(problem -> new ProblemPagingDto(
                        problem.getId(),
                        problem.getNumber(),
                        getStatus(problem),
                        problem.getTitle(),
                        problem.getAcceptance(),
                        problem.getDifficulty().getKorean(),
                        getTags(problem)
                ))
                .collect(Collectors.toList());

        CustomPageResponse<ProblemPagingDto> response = CustomPageResponse.<ProblemPagingDto>builder()
                .problems(dtos)
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
        return response;
    }

    public ProblemResponse readProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return ProblemResponse.builder()
                .number(problem.getNumber())
                .title(problem.getTitle())
                .status(getStatus(problem))
                .acceptance(problem.getAcceptance().toString())
                .difficulty(problem.getDifficulty().getKorean())
                .description(problem.getDescription())
                .defaultCodes(getDefaultCodes(problem))
                .testcases(getTestcases(problem))
                .tags(getTags(problem))
                .build();
    }

    private static List<String> getTags(Problem problem) {
        return problem.getProblemTags().stream()
                .map(ProblemTag::getTag)
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    private List<ProblemResponse.Testcase> getTestcases(Problem problem) {
        return problem.getTestcases().stream()
                .map((t) ->
                        ProblemResponse.Testcase.builder()
                                .number(t.getNumber())
                                .inputs(getInput(t))
                                .expected(t.getOutput())
                                .build()
                ).collect(Collectors.toList());
    }

    private static Map<String, String> getDefaultCodes(Problem problem) {
        return problem.getAvailableLanguages().stream()
                .collect(Collectors.toMap(
                        l -> l.getProgrammingLanguage().getName(),
                        AvailableLanguage::getTemplateCode
                ));
    }

    private String getStatus(Problem problem) {
        if (problem.getSubmits().isEmpty()) {
            return "미해결";
        }

        boolean isSuccess = problem.getSubmits().stream()
                .map(Submit::getResult)
                .anyMatch(r -> r.equals(Result.RESOLVED));

        if (isSuccess) {
            return "성공";
        }

        return "실패";
    }

    private List<InputDto> getInput(Testcase testcase) {
        List<InputDto> inputs = new ArrayList<>();
        String[] inputNames = testcase.getInputNames().split(",");
        String[] inputValues = testcase.getInputValues().split(",");
        for (int i = 0; i < inputNames.length; i++) {
            String name = inputNames[i];
            String value = inputValues[i];
            InputDto input = InputDto.builder()
                    .name(name)
                    .value(value)
                    .build();
            inputs.add(input);
        }

        return inputs;
    }

    @Transactional
    public Problem createProblem(String title, String description, String difficulty) {
        int lastProblemNumber = getLastProblemNumber() + 1;

        Problem problem = Problem.defaultBuilder()
                .number(lastProblemNumber + 1)
                .title(title)
                .description(description)
                .difficulty(Difficulty.getByKorean(difficulty))
                .build();

        return problemRepository.save(problem);
    }

    private Integer getLastProblemNumber() {
        return problemRepository.findMaxNumber()
                .orElse(0);
    }

}
