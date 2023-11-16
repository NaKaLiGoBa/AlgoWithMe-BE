package com.nakaligoba.backend.service.component.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    private List<Message> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private List<Content> content;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Content {
            private String type;
            private Text text;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Text {
                private String value;
            }
        }
    }
}
