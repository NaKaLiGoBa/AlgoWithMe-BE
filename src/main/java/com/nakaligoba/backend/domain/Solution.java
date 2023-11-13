package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.global.BaseEntity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolutionLanguage> solutionLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "solution")
    private List<SolutionLike> solutionLikes = new ArrayList<>();

    @OneToMany(mappedBy = "solution")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Solution(String title, String content, Member member, Problem problem, Long viewCount) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.problem = problem;
        this.viewCount = viewCount;
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
