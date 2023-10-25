package com.nakaligoba.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakaligoba.backend.entity.ChatMessage;
import com.nakaligoba.backend.service.ChatMessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        MessageDto dto = objectMapper.readValue(payload, MessageDto.class);
        ChatMessageService.ChatRoom room = chatMessageService.findRoomById(dto.getRoomId()); // 프로젝트 단위로 서치
        room.handleActionsByType(session, dto, chatMessageService);
    }


    @Getter
    @Setter
    public static class MessageDto {

        private ChatMessage.MessageType type;
        private Long roomId;
        private String sender;
        private String message;
        private String date;
    }
}
