package com.epam.eventsourcing.eventhandler;

import com.epam.eventsourcing.event.PersonCreated;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonCreatedEventHandler implements PersonEventHandler {

    private final PersonViewRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonCreatedEventHandler(PersonViewRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(Class<? extends PersonEvent> clazz) {
        return PersonCreated.class.isAssignableFrom(clazz);
    }

    @Override
    public void handle(PersonEvent event) {
        PersonCreated personCreated = (PersonCreated) event;
        log.info("Handle " + personCreated);

        Person personEventRecord = Person.builder()
                .id(personCreated.getId())
                .name(personCreated.getName())
                .gender(personCreated.getGender())
                .email(personCreated.getEmail())
                .build();
        repository.save(personEventRecord);
    }
}
