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

    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Submit(String code, Result result, Problem problem, Member member) {
        this.code = code;
        this.result = result;
        this.problem = problem;
        this.member = member;
    }

    public boolean isSuccess() {
        return result == Result.RESOLVED;
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
