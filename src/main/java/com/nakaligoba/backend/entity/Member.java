package com.nakaligoba.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@NoArgsConstructor
@Getter
@Setter
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

    @Builder
    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.nickname = name;
    }
}
