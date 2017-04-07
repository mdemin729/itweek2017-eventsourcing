package com.epam.eventsourcing.eventhandler;

import com.epam.eventsourcing.event.PersonEmailChanged;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class PersonEmailChangedEventHandler implements PersonEventHandler{

    private final PersonViewRepository repository;

    @Autowired
    public PersonEmailChangedEventHandler(PersonViewRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<? extends PersonEvent> clazz) {
        return PersonEmailChanged.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional
    public void handle(PersonEvent event) {
        PersonEmailChanged personEmailChanged = (PersonEmailChanged) event;
        log.info("Handle " + personEmailChanged);

        Person person = repository.findOne(personEmailChanged.getId());
        if (person != null) {
            person.setEmail(personEmailChanged.getEmail());
            repository.save(person);
        }
    }
}
