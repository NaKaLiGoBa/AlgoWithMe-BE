package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.global.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "submits")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private Result result;

    @Column(name = "runtime", nullable = false)
    private String runtime;

    @Column(name = "memory", nullable = false)
    private String memory;

    @Column(name = "timeComplexity", nullable = false)
    private String timeComplexity;

    @Column(name = "spaceComplexity", nullable = false)
    private String spaceComplexity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Submit(String code, Language language, Result result, Problem problem, Member member, String runtime, String memory, String timeComplexity, String spaceComplexity) {
        this.code = code;
        this.language = language;
        this.result = result;
        this.problem = problem;
        this.member = member;
        this.runtime = runtime;
        this.memory = memory;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }

    public boolean isSuccess() {
        return result == Result.RESOLVED;
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
