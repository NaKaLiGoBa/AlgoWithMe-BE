package com.nakaligoba.backend.domain;

import java.util.Arrays;
import java.util.Optional;

public enum Question {
    EDGE_CASE(
            "엣지 케이스",
            "find edge cases",
            ">input: \n>output: ",
            true,
            false,
            Assistant.EDGE_CASE_FINDER),
    CODE_STRUCTURE(
            "코드 구조와 가독성",
            "refactor code structure",
            "``` ```",
            false,
            true,
            Assistant.CODE_OPTIMIZER),
    CODE_OPTIMIZATION(
            "코드 최적화",
            "optimize given code",
            "``` ```",
            false,
            true,
            Assistant.CODE_OPTIMIZER),
    ALGORITHM_SELECTION(
            "알고리즘 선택과 설계",
            "select an appropriate algorithm and data structure to solve this problem.",
            "your message format is\n```Data Structure: []\nAlgorithm: []```",
            true,
            false,
            Assistant.ALGORITHM_SELECTOR),
    COUNTER_EXAMPLE("반례",
            "",
            "",
            true,
            true,
            null);

    private final String korean;
    private final String question;
    private final String answerFormat;
    private final boolean isRequireProblem;
    private final boolean isRequireCode;
    private final Assistant assistant;

    Question(String korean, String question, String answerFormat, boolean isRequireProblem, boolean isRequireCode, Assistant assistant) {
        this.korean = korean;
        this.question = question;
        this.answerFormat = answerFormat;
        this.isRequireProblem = isRequireProblem;
        this.isRequireCode = isRequireCode;
        this.assistant = assistant;
    }

    public static Optional<Question> findByKorean(String korean) {
        return Arrays.stream(Question.values())
                .filter(q -> q.korean.equals(korean))
                .findAny();
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerFormat() {
        return answerFormat;
    }

    public boolean isRequireProblem() {
        return isRequireProblem;
    }

    public boolean isRequireCode() {
        return isRequireCode;
    }

    public Assistant getAssistant() {
        return assistant;
    }
}
