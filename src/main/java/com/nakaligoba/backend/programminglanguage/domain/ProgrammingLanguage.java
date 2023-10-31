package com.nakaligoba.backend.programminglanguage.domain;

import com.nakaligoba.backend.availablelanguage.domain.AvailableLanguage;
import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programming_languages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammingLanguage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "programmingLanguage")
    private List<AvailableLanguage> availableLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "programmingLanguage")
    private List<SolutionLanguage> solutionLanguages = new ArrayList<>();
}
