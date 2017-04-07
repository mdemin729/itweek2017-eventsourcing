package com.epam.eventsourcing.replay;

import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class PersonPlaybackFunction {

    private final ImmutableMap<Class, BiFunction<PersonEventRecord, Person, InstantView<Person>>> eventFunctions;

    @Autowired
    public PersonPlaybackFunction(List<PersonEventInstantViewFunction> instantViewFunctions) {
        ImmutableMap.Builder<Class, BiFunction<PersonEventRecord, Person, InstantView<Person>>> builder =
                ImmutableMap.builder();
        for (PersonEventInstantViewFunction instantViewFunction : instantViewFunctions) {
            builder.put(instantViewFunction.supports(), instantViewFunction);
        }
        eventFunctions = builder.build();
    }

    public InstantView<Person> apply(PersonEventRecord eventRecord, Person person) {
        BiFunction<PersonEventRecord, Person, InstantView<Person>> function =
                eventFunctions.get(eventRecord.getData().getClass());
        if (function != null) {
            return function.apply(eventRecord, person);
        } else {
            return InstantView.<Person>builder()
                    .timestamp(eventRecord.getTimestamp())
                    .entity(person)
                    .build();
        }
    }


}
