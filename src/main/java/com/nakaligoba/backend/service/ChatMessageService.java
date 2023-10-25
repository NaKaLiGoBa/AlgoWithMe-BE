package com.nakaligoba.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakaligoba.backend.entity.ChatMessage;
import com.nakaligoba.backend.handler.WebSocketHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private static Long id = 0L;
    private final ObjectMapper objectMapper;
    private Map<Long, ChatRoom> chatRooms; // 추후에 리파지토리로 변경

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
        createRoom("AllChannel");
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(Long id) {
        return chatRooms.get(id);
    }

    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .projectId(id)
                .projectName(name)
                .build();
        chatRooms.put(id++, chatRoom);
        return chatRoom;
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Getter
    public static class ChatRoom {
        private Long id;
        private String name;
        private Set<WebSocketSession> sessions = new HashSet<>();

        @Builder
        public ChatRoom(Long projectId, String projectName) {
            this.id = projectId;
            this.name = projectName;
        }

        public void handleActionsByType(WebSocketSession session,
                                        WebSocketHandler.MessageDto chatMessage,
                                        ChatMessageService chatMessageService) {
            if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
                sessions.add(session);
                chatMessage.setMessage("입장했습니다.");
                sendMessageToAllInSession(chatMessage, chatMessageService);

            } else if (chatMessage.getType().equals(ChatMessage.MessageType.EXIT)) {
                chatMessage.setMessage("퇴장했습니다.");
                sendMessageToAllInSession(chatMessage, chatMessageService);
                sessions.remove(session);

            } else if (chatMessage.getType().equals(ChatMessage.MessageType.MESSAGE)) {
                sendMessageToAllInSession(chatMessage, chatMessageService);
            }
        }

        public <T> void sendMessageToAllInSession(T message, ChatMessageService chatMessageService) {
            sessions.parallelStream().forEach(session -> chatMessageService.sendMessage(session, message));
        }
    }


}
