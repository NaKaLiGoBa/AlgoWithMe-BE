package com.nakaligoba.backend.problemtag.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.tag.domain.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "problem_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public ProblemTag(Problem problem, Tag tag) {
        this.problem = problem;
        this.tag = tag;
    }

    public void setProblem(final Problem problem) {
        this.problem = problem;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
