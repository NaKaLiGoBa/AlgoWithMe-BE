package com.nakaligoba.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "testcases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Testcase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "input", nullable = false)
    private String input;

    @Column(name = "output", nullable = false)
    private String output;

    @Column(name = "isPublic", nullable = false)
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}
