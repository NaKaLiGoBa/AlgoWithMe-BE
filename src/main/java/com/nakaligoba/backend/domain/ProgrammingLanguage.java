package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Table(name = "programming_languages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrammingLanguage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language name;

    @OneToMany(mappedBy = "programmingLanguage")
    private List<AvailableLanguage> availableLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "programmingLanguage")
    private List<SolutionLanguage> solutionLanguages = new ArrayList<>();

    public ProgrammingLanguage(String name) {
        this.name = Language.findByName(name)
                .orElseThrow(NoSuchElementException::new);
    }
}
