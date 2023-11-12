package com.nakaligoba.backend.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mini_quizzes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MiniQuizTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mini_quiz_id", nullable = false)
    private MiniQuiz miniQuiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public MiniQuizTag(MiniQuiz miniQuiz, Tag tag) {
        this.miniQuiz = miniQuiz;
        this.tag = tag;
    }
}
