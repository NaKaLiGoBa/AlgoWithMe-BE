package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.global.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "solution_languages")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SolutionLanguage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Solution solution;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "programming_language_id")
//    private ProgrammingLanguage programmingLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaialbe_language_id")
    private AvailableLanguage availableLanguage;

    @Builder
    public SolutionLanguage(Solution solution, AvailableLanguage availableLanguage) {
        this.solution = solution;
        this.availableLanguage = availableLanguage;
    }
}
