package com.nakaligoba.backend.submit.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.problem.domain.Problem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "submits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "result", nullable = false)
    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
