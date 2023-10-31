package com.nakaligoba.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "solution_languages")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SolutionLanguage {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programming_language_id")
    private ProgrammingLanguage programmingLanguage;
}
