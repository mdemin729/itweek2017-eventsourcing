package com.epam.eventsourcing;

import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.eventhandler.PersonEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PersonEventListener {

    private final List<PersonEventHandler> handlers;

    @Autowired
    public PersonEventListener(List<PersonEventHandler> handlers) {
        this.handlers = handlers;
    }

    @KafkaListener(topics = "events")
    public void listen(PersonEvent event) {
        handlers.stream()
                .filter(personEventHandler -> personEventHandler.supports(event.getClass()))
                .forEach(personEventHandler -> personEventHandler.handle(event));
    }
}
