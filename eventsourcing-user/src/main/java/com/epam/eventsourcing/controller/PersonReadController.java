package com.epam.eventsourcing.controller;

import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class PersonReadController {

    private final PersonViewRepository personViewRepository;

    @Autowired
    public PersonReadController(PersonViewRepository personViewRepository) {
        this.personViewRepository = personViewRepository;
    }

    @GetMapping("/person")
    public List<Person> list() {
        return personViewRepository.findAll();
    }

    @GetMapping("/person/{id}")
    public Person findOne(@PathVariable UUID id) {
        return personViewRepository.findOne(id);
    }

}
