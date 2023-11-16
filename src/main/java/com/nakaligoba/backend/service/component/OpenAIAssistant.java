package com.nakaligoba.backend.service.component;

import com.nakaligoba.backend.domain.Assistant;
import com.nakaligoba.backend.exception.ExternalApiCallException;
import com.nakaligoba.backend.service.component.openai.Messages;
import com.nakaligoba.backend.service.component.openai.RunObject;
import com.nakaligoba.backend.service.component.openai.ThreadRun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class OpenAIAssistant {

    private static final String THREAD_RUN_URL = "https://api.openai.com/v1/threads/runs";

    @Value("${openai.api-key}")
    private String secret;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(secret);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("OpenAI-Beta", "assistants=v1");
                })
                .build();
    }

    public String answer(Assistant assistant, Collection<String> contents) {
        List<ThreadRun.Message> messages = new ArrayList<>();
        for (String content : contents) {
            ThreadRun.Message message = new ThreadRun.Message("user", content);
            messages.add(message);
            log.info("User Message: {}", content);
        }
        ThreadRun.Thread thread = new ThreadRun.Thread(messages);
        ThreadRun threadRun = new ThreadRun(assistant.getId(), thread);

        RunObject runObject = webClient.post()
                .uri(THREAD_RUN_URL)
                .bodyValue(threadRun)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RunObject.class)
                .blockOptional()
                .orElseThrow(ExternalApiCallException::new);

        while(isRunning(runObject)) {
            sleep(8000);
            String threadId = runObject.getThreadId();
            String runId = runObject.getId();
            runObject = webClient.get()
                    .uri("https://api.openai.com/v1/threads/" + threadId + "/runs/" + runId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(RunObject.class)
                    .blockOptional()
                    .orElseThrow(ExternalApiCallException::new);
        }

        String listMessageUrl = "https://api.openai.com/v1/threads/" + runObject.getThreadId() + "/messages?limit=1&sort=desc";

        List<Messages.Message> data = webClient.get()
                .uri(listMessageUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Messages.class)
                .blockOptional()
                .orElseThrow(ExternalApiCallException::new)
                .getData();
        for (Messages.Message datum : data) {
            log.info("{}", datum.getContent().get(0).getText().getValue());
        }

        Messages.Message assistantMessage = data
                .get(0);

        return assistantMessage.getContent()
                .get(0)
                .getText()
                .getValue();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isRunning(RunObject runObject) {
        String threadId = runObject.getThreadId();
        String status = runObject.getStatus();
        if (status.equals("failed")) {
            log.warn("AI Thread: {} ,Status: {}", threadId, status);
            throw new ExternalApiCallException();
        }
        log.info("AI Thread: {} ,Status: {}", threadId, status);
        return !runObject.getStatus().equals("completed");
    }
}
