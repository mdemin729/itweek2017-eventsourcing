package com.epam.eventsourcing.controller;

import com.epam.eventsourcing.event.PersonCreated;
import com.epam.eventsourcing.event.PersonEmailChanged;
import com.epam.eventsourcing.event.PersonNameChanged;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import com.epam.eventsourcing.jpa.PersonEventRepository;
import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.replay.PersonPlaybackFunction;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@RestController
public class ReplayController {

    private final PersonEventRepository repository;
    private final PersonPlaybackFunction personPlaybackFunction;

    @Autowired
    public ReplayController(PersonEventRepository repository,
                            PersonPlaybackFunction personPlaybackFunction) {
        this.repository = repository;
        this.personPlaybackFunction = personPlaybackFunction;
    }

    @GetMapping("/replay/{id}")
    List<InstantView<Person>> replay(@PathVariable UUID id) {

        PersonEventAccumulator accumulator = new PersonEventAccumulator();
        List<InstantView<Person>> history = repository.findByAggregateIdOrderById(id).stream()
                .collect(ArrayList::new,
                        accumulator,
                        List::addAll);
        return history;
    }

    class PersonEventAccumulator implements BiConsumer<List<InstantView<Person>>, PersonEventRecord> {

        private Person person = null;

        private ImmutableMap<Class, BiFunction<PersonEventRecord, Person, InstantView<Person>>> handlers =
                ImmutableMap.of(
                        PersonCreated.class, ((eventRecord, person1) -> {
                            PersonCreated created = (PersonCreated) eventRecord.getData();
                            Person newPerson = Person.builder()
                                    .id(eventRecord.getAggregateId())
                                    .name(created.getName())
                                    .email(created.getEmail())
                                    .gender(created.getGender())
                                    .build();
                            return InstantView.<Person>builder()
                                    .timestamp(eventRecord.getTimestamp())
                                    .entity(newPerson)
                                    .build();
                        }),
                        PersonNameChanged.class, ((eventRecord, person1) -> {
                            PersonNameChanged nameChanged = (PersonNameChanged) eventRecord.getData();
                            Person newPerson = person1.toBuilder().name(nameChanged.getName()).build();
                            return InstantView.<Person>builder()
                                    .timestamp(eventRecord.getTimestamp())
                                    .entity(newPerson)
                                    .build();
                        }),
                        PersonEmailChanged.class, ((eventRecord, person1) -> {
                            PersonEmailChanged nameChanged = (PersonEmailChanged) eventRecord.getData();
                            Person newPerson = person1.toBuilder().email(nameChanged.getEmail()).build();
                            return InstantView.<Person>builder()
                                    .timestamp(eventRecord.getTimestamp())
                                    .entity(newPerson)
                                    .build();
                        })
                );

        @Override
        public void accept(List<InstantView<Person>> instantViews, PersonEventRecord event) {
            InstantView<Person> instantView = personPlaybackFunction.apply(event, person);
            instantViews.add(instantView);
            person = instantView.getEntity();
        }
    }

}
