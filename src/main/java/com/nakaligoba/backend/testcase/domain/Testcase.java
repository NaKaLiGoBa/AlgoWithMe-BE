package com.nakaligoba.backend.testcase.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.problem.domain.Problem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "testcases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Testcase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "input_names", nullable = false)
    private String inputNames;

    @Column(name = "input_values", nullable = false)
    private String inputValues;

    @Column(name = "output", nullable = false)
    private String output;

    @Column(name = "isGrading", nullable = false)
    private Boolean isGrading;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}
