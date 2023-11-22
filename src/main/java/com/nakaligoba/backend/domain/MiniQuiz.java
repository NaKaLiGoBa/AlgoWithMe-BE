package com.nakaligoba.backend.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mini_quizzes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MiniQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "choice_or_initials")
    private String choiceOrInitials;

    @Column(name = "description")
    private String description;

    @Column(name = "`explain`")
    private String explain;

    @Column(name = "answer")
    private String answer;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MiniQuizType miniQuizType;

    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Builder
    public MiniQuiz(String choiceOrInitials, String description, String explain, String answer, MiniQuizType miniQuizType, Difficulty difficulty) {
        this.choiceOrInitials = choiceOrInitials;
        this.description = description;
        this.explain = explain;
        this.answer = answer;
        this.miniQuizType = miniQuizType;
        this.difficulty = difficulty;
    }
}
