package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.SolutionRepository;
import com.nakaligoba.backend.repository.TagRepository;
import com.nakaligoba.backend.service.dto.InputDto;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final TestcaseService testcaseService;

    private final SolutionRepository solutionRepository;
    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;

    public CustomPageResponse<ProblemPagingDto> getProblemList(Pageable pageable, Optional<String> status, Optional<String> difficulty, Optional<List<String>> tags) {


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

}
