package com.nakaligoba.backend.solution.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguage;
import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.problem.domain.Problem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "solutions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Solution extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "solution")
    private List<SolutionLanguage> solutionLanguages = new ArrayList<>();
}
