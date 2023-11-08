package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Language;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.domain.ProblemTag;
import com.nakaligoba.backend.domain.Result;
import com.nakaligoba.backend.domain.Submit;
import com.nakaligoba.backend.domain.Tag;
import com.nakaligoba.backend.service.dto.InputDto;
import com.nakaligoba.backend.domain.Testcase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestcaseService testcaseService;

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

    private List<String> getTags(Problem problem) {
        return problem.getProblemTags().stream()
                .map(ProblemTag::getTag)
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    private List<ProblemResponse.TestcaseResponse> getTestcases(Problem problem) {
        return problem.getTestcases().stream()
                .map((t) ->
                        ProblemResponse.TestcaseResponse.builder()
                                .number(t.getNumber())
                                .inputs(getInput(t))
                                .expected(t.getOutput())
                                .build()
                ).collect(Collectors.toList());
    }

    private Map<String, String> getDefaultCodes(Problem problem) {
        Testcase testcase = problem.getTestcases()
                .stream()
                .findAny()
                .orElseThrow(NoSuchElementException::new);
        String[] args = testcase.getInputNamesAsList()
                .toArray(String[]::new);
        return Arrays.stream(Language.values())
                .collect(Collectors.toMap(
                        Language::getName,
                        l -> l.getDefaultCode(args)
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

    private List<ProblemResponse.InputResponse> getInput(Testcase testcase) {
        List<InputDto> inputs1 = testcaseService.getInputs(testcase);
        List<ProblemResponse.InputResponse> inputs = new ArrayList<>();
        for (InputDto inputDto : inputs1) {
            String name = inputDto.getName();
            String value = inputDto.getValue();
            ProblemResponse.InputResponse input = ProblemResponse.InputResponse.builder()
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

    public Optional<Problem> getProblem(Long id) {
        return problemRepository.findById(id);
    }

}
