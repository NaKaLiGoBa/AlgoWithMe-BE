package com.nakaligoba.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chat_messages")
@NoArgsConstructor
@Getter
@Setter // 잠시 테스트 용도
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY) // senderId
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY) // roomId
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    public enum MessageType {
        ENTER, MESSAGE, EXIT
    }
}
