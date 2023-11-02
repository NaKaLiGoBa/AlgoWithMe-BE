package com.nakaligoba.backend.solutionlanguage.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.solution.domain.Solution;
import lombok.*;

import javax.persistence.*;

@Table(name = "solution_languages")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SolutionLanguage extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programming_language_id")
    private ProgrammingLanguage programmingLanguage;

    @Builder
    public SolutionLanguage(Solution solution, ProgrammingLanguage programmingLanguage) {
        this.solution = solution;
        this.programmingLanguage = programmingLanguage;
    }
}
