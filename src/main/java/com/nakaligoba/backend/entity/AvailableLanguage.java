package com.nakaligoba.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "available_languages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AvailableLanguage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmingLanguage_id", nullable = false)
    private ProgrammingLanguage programmingLanguage;
}
