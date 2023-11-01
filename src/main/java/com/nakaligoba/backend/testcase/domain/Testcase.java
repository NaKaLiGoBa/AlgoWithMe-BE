package com.nakaligoba.backend.testcase.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.problem.domain.Problem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "testcases")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Testcase extends BaseEntity {

    private static final String INPUT_DELIMITER = " ";
    private static final String TESTCASE_DELIMITER = "\n";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;

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

    public static Testcase of(List<String> parameters, String testcase, boolean isGrading) {
        String inputNames = String.join(INPUT_DELIMITER, parameters);
        String[] split = testcase.split(TESTCASE_DELIMITER);
        String inputValues = split[0];
        String output = split[1];
        return Testcase.builder()
                .id(null)
                .number(null)
                .inputNames(inputNames)
                .inputValues(inputValues)
                .output(output)
                .isGrading(isGrading)
                .problem(null)
                .build();
    }

    public void setProblem(final Problem problem) {
        this.problem = problem;
    }
}
