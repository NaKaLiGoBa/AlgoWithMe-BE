package com.nakaligoba.backend.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "result", nullable = false)
    private String result;

    @OneToMany(mappedBy = "submission")
    private List<Submit> submits = new ArrayList<>();
}
