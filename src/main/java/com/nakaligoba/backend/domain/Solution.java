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
    private List<SolutionView> solutionViews = new ArrayList<>();

    @OneToMany(mappedBy = "solution")
    private List<Comment> comments = new ArrayList<>();

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

    public void updateSolutionLanguages(List<AvailableLanguage> availableLanguages) {
        this.solutionLanguages.clear();
        for (AvailableLanguage availableLanguage : availableLanguages) {
            this.solutionLanguages.add(new SolutionLanguage(this, availableLanguage));
        }
    }
}
