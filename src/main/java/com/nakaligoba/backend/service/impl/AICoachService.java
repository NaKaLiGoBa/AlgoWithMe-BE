package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.response.CoachAnswersResponse;
import com.nakaligoba.backend.domain.Answer;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Question;
import com.nakaligoba.backend.exception.ExternalApiCallException;
import com.nakaligoba.backend.repository.AnswerRepository;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.service.component.OpenAIAssistant;
import com.nakaligoba.backend.service.dto.AnswerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AICoachService {

    private final MemberService memberService;
    private final OpenAIAssistant openAIAssistant;

    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public AnswerDto createAnswer(String email, Long problemId, String koreanQuestion, String code) {
        Member member = memberService.getMemberByEmail(email);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);
        Question question = Question.findByKorean(koreanQuestion)
                .orElseThrow(NoSuchElementException::new);

        List<String> messages = new ArrayList<>();
        messages.add(question.getQuestion());
        messages.add(question.getAnswerFormat());

        if (question.isRequireProblem()) {
            messages.add(problem.getDescription());
        }
        if (question.isRequireCode()) {
            messages.add(code);
        }
        String aiAnswer;
        try {
            aiAnswer = openAIAssistant.answer(question.getAssistant(), messages);
        } catch (Exception ex) {
            log.error("OpenAI fail");
            log.error(ex.getMessage());
            aiAnswer = "적당한 답변입니다! AI는 죽어버렸어요!";
        }
        Answer answer = Answer.builder()
                .question(question)
                .answer(aiAnswer)
                .member(member)
                .problem(problem)
                .build();
        answerRepository.save(answer);

        return new AnswerDto(koreanQuestion, aiAnswer);
    }

    @Transactional(readOnly = true)
    public CoachAnswersResponse getAnswerList(String email, Long problemId) {
        Member member = memberService.getMemberByEmail(email);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);

        List<Answer> answers = answerRepository.findByMemberAndProblem(member, problem);

        List<CoachAnswersResponse.AnswerInfo> answerInfos = answers.stream()
                .map(answer -> CoachAnswersResponse.AnswerInfo.builder()
                        .question(answer.getQuestion().getQuestion())
                        .answer(answer.getAnswer())
                        .build())
                .collect(Collectors.toList());

        return CoachAnswersResponse.builder()
                .answerCount((long)answers.size())
                .answers(answerInfos)
                .build();
    }
}
