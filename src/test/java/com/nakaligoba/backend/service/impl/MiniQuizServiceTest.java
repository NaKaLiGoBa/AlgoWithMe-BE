package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.MiniQuiz;
import com.nakaligoba.backend.domain.MiniQuizTag;
import com.nakaligoba.backend.domain.MiniQuizType;
import com.nakaligoba.backend.domain.Tag;
import com.nakaligoba.backend.repository.MiniQuizRepository;
import com.nakaligoba.backend.repository.MiniQuizTagRepository;
import com.nakaligoba.backend.repository.TagRepository;
import com.nakaligoba.backend.service.dto.MiniQuizDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MiniQuizServiceTest {

    @Autowired
    ProblemFacade problemFacade;

    @Autowired
    MiniQuizRepository miniQuizRepository;

    @Autowired
    MiniQuizTagRepository miniQuizTagRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MiniQuizService miniQuizService;

    @Test
    @DisplayName("문제의 난이도와 태그에 맞는 미니퀴즈 읽어오기")
    void listMiniQuizzes() {
        CreateProblemRequest request = ProblemFixture.CREATE_PROBLEM_REQUEST;
        Long problemId = problemFacade.createProblem(request);
        List<Tag> tags = request.getTags()
                .stream()
                .map(tagRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Tag dfs = tags.stream()
                .filter(t -> t.getName().equals("DFS"))
                .findAny()
                .orElseThrow();
        Tag bfs = tags.stream()
                .filter(t -> t.getName().equals("BFS"))
                .findAny()
                .orElseThrow();
        Tag array = new Tag("Array");
        tagRepository.save(array);
        Difficulty difficulty = Difficulty.getByKorean(request.getDifficulty());
        problemFacade.createProblem(request);

        MiniQuiz dfsQuiz = MiniQuiz.builder()
                .description("DFS 결과는 비순환 구조를 갖는다.")
                .explain("DFS의 결과는 트리이다.")
                .answer("O")
                .miniQuizType(MiniQuizType.OX)
                .difficulty(difficulty)
                .choiceOrInitials("")
                .build();
        MiniQuizTag dfsQuizTag = new MiniQuizTag(dfsQuiz, dfs);
        miniQuizRepository.save(dfsQuiz);
        miniQuizTagRepository.save(dfsQuizTag);

        MiniQuiz bfsQuiz = MiniQuiz.builder()
                .description("너비 우선 탐색은?")
                .explain("Bandwidth Fisrt Search")
                .answer("BFS")
                .miniQuizType(MiniQuizType.CHOICE)
                .difficulty(difficulty)
                .choiceOrInitials("BFS,DFS,MST,BitMask")
                .build();
        MiniQuizTag bfsQuizTag = new MiniQuizTag(bfsQuiz, bfs);
        miniQuizRepository.save(bfsQuiz);
        miniQuizTagRepository.save(bfsQuizTag);

        MiniQuiz arrayQuiz = MiniQuiz.builder()
                .description("배열의 특정 위치를 가리키는 숫자의 이름은?")
                .explain("인덱스")
                .answer("인덱스")
                .miniQuizType(MiniQuizType.CHOICE)
                .difficulty(difficulty)
                .choiceOrInitials("ㅇ,ㄷ,ㅅ")
                .build();
        MiniQuizTag arrayQuizTag = new MiniQuizTag(bfsQuiz, array);
        miniQuizRepository.save(arrayQuiz);
        miniQuizTagRepository.save(arrayQuizTag);

        List<MiniQuizDto> miniQuizzes = miniQuizService.findAllQuizByProblemId(problemId);

        assertThat(miniQuizzes.size()).isEqualTo(2);
        assertThat(miniQuizzes.stream().map(MiniQuizDto::getType).collect(Collectors.toList())).contains("OX", "CHOICE");
    }
}
