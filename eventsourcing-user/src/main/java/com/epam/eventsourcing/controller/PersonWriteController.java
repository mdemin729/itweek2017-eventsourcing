package com.epam.eventsourcing.controller;

import com.epam.eventsourcing.EventPublisher;
import com.epam.eventsourcing.dto.CreatePerson;
import com.epam.eventsourcing.event.PersonCreated;
import com.epam.eventsourcing.event.PersonEmailChanged;
import com.epam.eventsourcing.event.PersonNameChanged;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
public class PersonWriteController {

    private final EventPublisher eventPublisher;

    @Autowired
    public PersonWriteController(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/person")
    public ResponseEntity createPerson(@Valid @RequestBody CreatePerson createPerson) {

        PersonCreated personCreated = PersonCreated.builder()
                .id(UUID.randomUUID())
                .name(createPerson.getName())
                .email(createPerson.getEmail())
                .gender(createPerson.getGender())
                .build();

        eventPublisher.publish(personCreated);
        log.info("Enqueue {}", personCreated);

        return ResponseEntity.accepted().location(personUri(personCreated.getId())).build();
    }

    @PutMapping("/person/{id}/name")
    public ResponseEntity changeName(@PathVariable UUID id,
                                     @RequestParam("value") String name) {

        PersonNameChanged personNameChanged = PersonNameChanged.builder().id(id).name(name).build();

        eventPublisher.publish(personNameChanged);
        log.info("Enqueue {}", personNameChanged);

        return ResponseEntity.accepted().location(personUri(id)).build();
    }

    @PutMapping("/person/{id}/email")
    public ResponseEntity changeEmail(@PathVariable UUID id,
                                      @RequestParam("value") String email) {

        PersonEmailChanged personEmailChanged = PersonEmailChanged.builder().id(id).email(email).build();

        eventPublisher.publish(personEmailChanged);
        log.info("Enqueue {}", personEmailChanged);

        return ResponseEntity.accepted().location(personUri(id)).build();
    }

    private URI personUri(UUID id) {
        return UriComponentsBuilder.fromPath("/person/")
                .path(id.toString())
                .build()
                .toUri();
    }

}
