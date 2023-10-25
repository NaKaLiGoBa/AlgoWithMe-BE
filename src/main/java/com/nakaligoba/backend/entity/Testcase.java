package com.nakaligoba.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "testcases")
@NoArgsConstructor
@Getter
public class Testcase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expected", nullable = false)
    private String expected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "testcases")
    private List<TestcaseInput> testcaseInputs = new ArrayList<>();

}
