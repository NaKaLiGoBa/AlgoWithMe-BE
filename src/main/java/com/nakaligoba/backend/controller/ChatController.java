package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.service.ChatMessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;

    @PostMapping
    public ChatRoomDTO createRoom(@RequestParam String name) {
        ChatMessageService.ChatRoom room = chatMessageService.createRoom(name);
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());

        return dto;
    }

    @GetMapping
    public List<ChatMessageService.ChatRoom> findAllRoom() {
        return chatMessageService.findAllRoom();
    }

    @Setter
    @Getter
    private class ChatRoomDTO {
        private Long id;
        private String name;
    }
}


