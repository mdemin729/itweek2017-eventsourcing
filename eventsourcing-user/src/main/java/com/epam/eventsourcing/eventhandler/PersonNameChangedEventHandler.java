package com.epam.eventsourcing.eventhandler;

import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.event.PersonNameChanged;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class PersonNameChangedEventHandler implements PersonEventHandler{

    private final PersonViewRepository repository;

    @Autowired
    public PersonNameChangedEventHandler(PersonViewRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<? extends PersonEvent> clazz) {
        return PersonNameChanged.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional
    public void handle(PersonEvent event) {
        PersonNameChanged personNameChanged = (PersonNameChanged) event;
        log.info("Handle " + personNameChanged);

        Person person = repository.findOne(personNameChanged.getId());
        if (person != null) {
            person.setName(personNameChanged.getName());
            repository.save(person);
        }
    }
}
