package com.epam.eventsourcing.replay;


import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.event.PersonCreated;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import org.springframework.stereotype.Component;

@Component
public class PersonChangedInstantViewFunction implements PersonEventInstantViewFunction {

    @Override
    public Class<? extends PersonEvent> supports() {
        return PersonCreated.class;
    }

    @Override
    public InstantView<Person> apply(PersonEventRecord eventRecord, Person person) {
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
    }
}
