package com.nakaligoba.backend.service.impl;


import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.MiniQuiz;
import com.nakaligoba.backend.domain.MiniQuizTag;
import com.nakaligoba.backend.domain.MiniQuizType;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.ProblemTag;
import com.nakaligoba.backend.domain.Tag;
import com.nakaligoba.backend.repository.MiniQuizRepository;
import com.nakaligoba.backend.repository.MiniQuizTagRepository;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.TagRepository;
import com.nakaligoba.backend.service.dto.MiniQuizDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiniQuizService {

    private final ProblemRepository problemRepository;
    private final MiniQuizTagRepository miniQuizTagRepository;
    private final MiniQuizRepository miniQuizRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<MiniQuizDto> findAllQuizByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);

        List<Tag> tags = problem.getProblemTags()
                .stream()
                .map(ProblemTag::getTag)
                .collect(Collectors.toList());
        Difficulty difficulty = problem.getDifficulty();

        return miniQuizTagRepository.findAllByTagIn(tags)
                .stream()
                .map(MiniQuizTag::getMiniQuiz)
                .filter(mq -> mq.getDifficulty().equals(difficulty))
                .map(this::domainToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createMiniQuiz(MiniQuizDto dto, String difficulty, List<String> tags) {
        MiniQuizType type;
        switch (dto.getType()) {
            case "ox":
                type = MiniQuizType.OX;
                break;
            case "choice":
                type = MiniQuizType.CHOICE;
                break;
            case "initial":
                type = MiniQuizType.INITIAL;
                break;
            default:
                throw new IllegalArgumentException("잘못된 문제 형식입니다.");
        }

        MiniQuiz miniQuiz = MiniQuiz.builder()
                .description(dto.getDescription())
                .answer(dto.getAnswer())
                .miniQuizType(type)
                .explain(dto.getExplain())
                .difficulty(Difficulty.getByKorean(difficulty))
                .choiceOrInitials(dto.getChoiceOrInitials().stream().collect(Collectors.joining(",")))
                .build();
        MiniQuiz savedMiniQuiz = miniQuizRepository.save(miniQuiz);
        List<Tag> miniQuizTags = tags.stream()
                .map(tagRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        for (Tag miniQuizTag : miniQuizTags) {
            miniQuizTagRepository.save(new MiniQuizTag(miniQuiz, miniQuizTag));
        }
        return savedMiniQuiz.getId();
    }

    private MiniQuizDto domainToDto(MiniQuiz miniQuiz) {
        List<String> choiceOrInitials = miniQuiz.getMiniQuizType()
                .mapToList(miniQuiz.getChoiceOrInitials());
        return MiniQuizDto.builder()
                .type(miniQuiz.getMiniQuizType().name())
                .answer(miniQuiz.getAnswer())
                .explain(miniQuiz.getExplain())
                .description(miniQuiz.getDescription())
                .choiceOrInitials(choiceOrInitials)
                .build();
    }
}
