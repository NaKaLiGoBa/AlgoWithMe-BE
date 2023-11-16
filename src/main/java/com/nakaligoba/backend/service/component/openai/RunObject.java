package com.nakaligoba.backend.service.component.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunObject {
    private String id;
    @JsonProperty("created_at") private Long createdAt;
    @JsonProperty("assistant_id") private String assistantId;
    @JsonProperty("thread_id") private String threadId;
    private String status;
}
