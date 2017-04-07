package com.epam.eventsourcing.controller;

import com.epam.eventsourcing.jpa.PersonEventRecord;
import com.epam.eventsourcing.jpa.PersonEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class PersonHistoryController {

    private final PersonEventRepository personEventRepository;

    @Autowired
    public PersonHistoryController(PersonEventRepository personEventRepository) {
        this.personEventRepository = personEventRepository;
    }

    @GetMapping("/personHistory/{id}")
    public List<PersonEventRecord> history(@PathVariable UUID id) {
        return personEventRepository.findByAggregateIdOrderById(id);
    }

}
