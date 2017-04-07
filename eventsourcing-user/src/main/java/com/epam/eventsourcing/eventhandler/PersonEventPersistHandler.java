package com.epam.eventsourcing.eventhandler;

import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import com.epam.eventsourcing.jpa.PersonEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonEventPersistHandler implements PersonEventHandler {

    private final PersonEventRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonEventPersistHandler(PersonEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(Class<? extends PersonEvent> clazz) {
        return true;
    }

    @Override
    public void handle(PersonEvent event) {

        PersonEventRecord personEventRecord = PersonEventRecord.builder()
                .aggregateId(event.getId())
                .data(event)
                .build();
        repository.save(personEventRecord);

        log.info("Persisted " + event);
    }
}
