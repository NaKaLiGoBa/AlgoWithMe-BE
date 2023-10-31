package com.nakaligoba.backend.member.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.solution.domain.Solution;
import com.nakaligoba.backend.submit.domain.Submit;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<Submit> submits = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Solution> solutions = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
