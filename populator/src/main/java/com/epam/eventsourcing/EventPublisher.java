package com.epam.eventsourcing;

import com.epam.eventsourcing.event.PersonEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final KafkaTemplate<String, PersonEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventPublisher(KafkaTemplate<String, PersonEvent> kafkaTemplate,
                          ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(PersonEvent event) {
        kafkaTemplate.sendDefault(event);
    }
}
