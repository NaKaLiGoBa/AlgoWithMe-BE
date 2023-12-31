package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.global.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "comments")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Comment(Long id, String content, Member member, Solution solution) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.solution = solution;
    }

    public void update(String content) {
        this.content = content;
    }
}
