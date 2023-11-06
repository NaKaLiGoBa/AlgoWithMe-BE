package com.nakaligoba.backend.solution.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguage;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "solutions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Builder
    public Solution(String title, String content, Member member, Problem problem) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.problem = problem;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }

    public void updateSolutionLanguages(List<ProgrammingLanguage> programmingLanguages) {
        this.solutionLanguages.clear();
        for (ProgrammingLanguage programmingLanguage : programmingLanguages) {
            this.solutionLanguages.add(new SolutionLanguage(this, programmingLanguage));
        }
    }
}
