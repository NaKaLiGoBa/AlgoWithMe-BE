package com.nakaligoba.backend.service.component.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ThreadRun {
    @JsonProperty("assistant_id") private final String assistantId;
    private final Thread thread;

    @Data
    public static class Thread {
        private final List<Message> messages;
    }

    @Data
    public static class Message {
        private final String role;
        private final String content;
    }
}
