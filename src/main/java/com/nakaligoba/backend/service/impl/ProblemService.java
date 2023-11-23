package com.nakaligoba.backend.service.impl;


import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.Language;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.ProblemTag;
import com.nakaligoba.backend.domain.Submit;
import com.nakaligoba.backend.domain.Tag;
import com.nakaligoba.backend.domain.Testcase;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.SolutionRepository;
import com.nakaligoba.backend.repository.SubmitRepository;
import com.nakaligoba.backend.repository.TagRepository;
import com.nakaligoba.backend.service.dto.InputDto;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final TestcaseService testcaseService;

    private final SolutionRepository solutionRepository;
    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;
    private final SubmitRepository submitRepository;

    public CustomPageResponse<ProblemPagingDto> getProblemList(Pageable pageable, String status, String difficulty, List<String> tags) {
        Page<Problem> page = problemRepository.findAll(pageable);
        Stream<Problem> problemStream = page.getContent().stream();

        if (Objects.nonNull(status)) {
            problemStream = problemStream.filter(problem -> {
                List<Submit> submits = submitRepository.findAllByProblem(problem);

                switch (status) {
                    case "성공":
                        return submits.stream().anyMatch(Submit::isSuccess);
                    case "미해결":
                        return submits.isEmpty();
                    case "실패":
                        return submits.stream().anyMatch(Submit::isFail);
                    default:
                        return true;
                }
            });
        }

        if (Objects.nonNull(difficulty)) {
            Difficulty difficultyEnum = Difficulty.getByKorean(difficulty);
            problemStream = problemStream.filter(problem -> problem.getDifficulty() == difficultyEnum);
        }

        if (Objects.nonNull(tags) && !tags.isEmpty()) {
            Set<String> tagsSet = new HashSet<>(tags);
            problemStream = problemStream.filter(problem -> getTags(problem).stream().anyMatch(tagsSet::contains));
        }

        List<ProblemPagingDto> dtos = problemStream
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
                .status(List.of("성공", "실패", "미해결"))
                .difficulties(List.of("쉬움", "보통", "어려움"))
                .tags(tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toList()))
                .build();
        return response;
    }

    public ProblemResponse readProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

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
                .totalSolutionCount(solutionRepository.countByProblem(problem))
                .easierProblemUrl(getEasierProblems(problem))
                .harderProblemUrl(getHarderProblems(problem))
                .build();
    }

    private String getEasierProblems(Problem currentProblem) {
        List<Problem> selectedProblems = getProblemsByTags(currentProblem);
        String currentProblemDifficulty = currentProblem.getDifficulty().getKorean();
        String problemUrl = "";

        if (Difficulty.HARD.getKorean().equals(currentProblemDifficulty)) {
            problemUrl = getProblemUrlsByDifficulty(Difficulty.MEDIUM.getKorean(), currentProblem.getId(), selectedProblems);
        } else {
            problemUrl = getProblemUrlsByDifficulty(Difficulty.EASY.getKorean(), currentProblem.getId(), selectedProblems);
        }

        return problemUrl;
    }

    private String getHarderProblems(Problem currentProblem) {
        List<Problem> selectedProblems = getProblemsByTags(currentProblem);
        String currentProblemDifficulty = currentProblem.getDifficulty().getKorean();
        String problemUrl = "";

        if (Difficulty.EASY.getKorean().equals(currentProblemDifficulty)) {
            problemUrl = getProblemUrlsByDifficulty(Difficulty.MEDIUM.getKorean(), currentProblem.getId(), selectedProblems);
        } else {
            problemUrl = getProblemUrlsByDifficulty(Difficulty.HARD.getKorean(), currentProblem.getId(), selectedProblems);
        }

        return problemUrl;
    }

    private List<Problem> getProblemsByTags(Problem currentProblem) {
        return problemRepository.findAll()
                .stream()
                .filter(p -> p.getProblemTags().stream()
                        .anyMatch(pt ->
                                currentProblem.getProblemTags().stream()
                                        .anyMatch(problemTag ->
                                                problemTag.getTag().getName().equals(pt.getTag().getName())
                                        )
                        )
                )
                .collect(Collectors.toList());
    }

    private String getProblemUrlsByDifficulty(String difficulty, Long currentProblemId, List<Problem> selectedProblems) {
        String baseUrl = "https://kde05c63df3aaa.user-app.krampoline.com/api/v1/problems/";

        List<String> problemUrls = selectedProblems.stream()
                .filter(p -> difficulty.equals(p.getDifficulty().getKorean())
                        && (currentProblemId.compareTo(p.getId()) != 0))
                .map(sp -> baseUrl + sp.getId())
                .collect(Collectors.toList());

        /* 여러 개 줄 경우
        if (problemUrls.size() > 5) {
            List<String> newProblemUrls = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                newProblemUrls.add(problemUrls.get(i));
            }

            return String.join(",", newProblemUrls);
        }

        return String.join(",", problemUrls);
         */

        if(problemUrls.size() == 0) {
            return "";
        }

        return problemUrls.get(0);
    }

    private List<String> getTags(Problem problem) {
        return problem.getProblemTags().stream()
                .map(ProblemTag::getTag)
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    private List<ProblemResponse.TestcaseResponse> getTestcases(Problem problem) {
        return problem.getTestcases().stream()
                .filter(Testcase::isTesting)
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

    @Transactional
    public String getStatus(Problem problem) {
        List<Submit> submits = submitRepository.findAllByProblem(problem);
        if (submits.isEmpty()) {
            return "미해결";
        }

        boolean isSuccess = submits.stream()
                .map(Submit::isSuccess)
                .reduce(false, Boolean::logicalOr);

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
        int lastProblemNumber = getLastProblemNumber();

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

    @Transactional(readOnly = true)
    public Problem getProblem(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public BigDecimal getAcceptance(Long problemId) {
        Problem findProblem = problemRepository.findById(problemId).orElseThrow();
        List<Submit> findProblemSubmits = findProblem.getSubmits();

        long totalSubmits = findProblemSubmits.size();
        if (totalSubmits == 0) {
            return BigDecimal.ZERO;
        }

        long successfulSubmits = findProblemSubmits.stream()
                .filter(Submit::isSuccess)
                .count();

        return BigDecimal.valueOf(successfulSubmits)
                .divide(BigDecimal.valueOf(totalSubmits), 2, RoundingMode.HALF_UP);
    }
    @Transactional
    public void updateProblemAcceptance(Long problemId) {
        BigDecimal acceptanceRate = getAcceptance(problemId);
        Problem problem = problemRepository.findById(problemId).orElseThrow();
        problem.updateAcceptance(acceptanceRate);
    }
}
