package com.epam.eventsourcing.replay;


import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.event.PersonNameChanged;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import org.springframework.stereotype.Component;

@Component
public class PersonNameChangedInstantViewFunction implements PersonEventInstantViewFunction {

    @Override
    public Class<? extends PersonEvent> supports() {
        return PersonNameChanged.class;
    }

    @Override
    public InstantView<Person> apply(PersonEventRecord eventRecord, Person person) {
        PersonNameChanged nameChanged = (PersonNameChanged) eventRecord.getData();

        Person newPerson = person.toBuilder()
                .name(nameChanged.getName())
                .build();

        return InstantView.<Person>builder()
                .timestamp(eventRecord.getTimestamp())
                .entity(newPerson)
                .build();
    }
}
