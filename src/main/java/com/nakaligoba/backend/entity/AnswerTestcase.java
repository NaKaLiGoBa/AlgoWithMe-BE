package com.nakaligoba.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answer_testcases")
@NoArgsConstructor
@Getter
public class AnswerTestcase extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "output", nullable = false)
    private String output;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "answerTestcase")
    private List<AnswerTestcaseInput> answerTestcaseInputs = new ArrayList<>();
}
