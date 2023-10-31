package com.nakaligoba.backend.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problems")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private Long number;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "acceptance", nullable = false)
    private BigDecimal acceptance;

    @OneToMany(mappedBy = "problem")
    private List<Submit> submits = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private List<Testcase> testcases = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private List<AvailableLanguage> availableLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private List<ProblemTag> problemTags = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private List<Solution> solutions = new ArrayList<>();

    public Problem(Long number, String description, String title, String difficulty, BigDecimal acceptance) {
        this.number = number;
        this.description = description;
        this.title = title;
        this.difficulty = difficulty;
        this.acceptance = acceptance;
    }
}
