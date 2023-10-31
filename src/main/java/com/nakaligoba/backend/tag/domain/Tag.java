package com.nakaligoba.backend.tag.domain;

import com.nakaligoba.backend.global.BaseEntity;
import com.nakaligoba.backend.problemtag.domain.ProblemTag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ProblemTag> problemTags = new ArrayList<>();
}
