package com.epam.eventsourcing.replay;

import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.event.PersonEmailChanged;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import org.springframework.stereotype.Component;

@Component
public class PersonEmailChangedInstantViewFunction implements PersonEventInstantViewFunction {

    @Override
    public Class<? extends PersonEvent> supports() {
        return PersonEmailChanged.class;
    }

    @Override
    public InstantView<Person> apply(PersonEventRecord eventRecord, Person person) {
        PersonEmailChanged emailChanged = (PersonEmailChanged) eventRecord.getData();

        Person newPerson = person.toBuilder()
                .email(emailChanged.getEmail())
                .build();

        return InstantView.<Person>builder()
                .timestamp(eventRecord.getTimestamp())
                .entity(newPerson)
                .build();
    }
}
