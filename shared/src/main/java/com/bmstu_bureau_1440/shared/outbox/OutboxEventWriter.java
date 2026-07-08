package com.bmstu_bureau_1440.shared.outbox;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OutboxEventWriter {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;

    public void enqueue(UUID aggregateId, String eventType, String topic, Object event) {
        outboxEventRepository.save(new OutboxEvent(
                aggregateId,
                eventType,
                topic,
                objectMapper.writeValueAsString(event)));
    }

}
