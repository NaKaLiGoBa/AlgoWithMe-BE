package com.nakaligoba.backend.problem.domain;


import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguage;
import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.problemtag.domain.ProblemTag;
import com.nakaligoba.backend.solution.domain.Solution;
import com.nakaligoba.backend.submit.domain.Submit;
import com.nakaligoba.backend.testcase.domain.Testcase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Integer number;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "acceptance", nullable = false)
    private BigDecimal acceptance;

    @OneToMany(mappedBy = "problem")
    private final List<Submit> submits = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private final List<Testcase> testcases = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private final List<AvailableLanguage> availableLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private final List<ProblemTag> problemTags = new ArrayList<>();

    @OneToMany(mappedBy = "problem")
    private final List<Solution> solutions = new ArrayList<>();

    @Builder(builderMethodName = "defaultBuilder")
    public static Problem of(final Integer number,
                             final String title,
                             final String description,
                             final Difficulty difficulty) {
        Problem newInstance = new Problem();
        newInstance.number = number;
        newInstance.title = title;
        newInstance.description = description;
        newInstance.difficulty = difficulty;
        newInstance.acceptance = BigDecimal.ZERO;
        return newInstance;
    }

    public void addTestcase(final Testcase testcase) {
        this.testcases.add(testcase);
        testcase.setProblem(this);
    }
}
