package com.epam.eventsourcing.replay;

import com.epam.eventsourcing.dto.InstantView;
import com.epam.eventsourcing.event.PersonEvent;
import com.epam.eventsourcing.jpa.Person;
import com.epam.eventsourcing.jpa.PersonEventRecord;

import java.util.function.BiFunction;

public interface PersonEventInstantViewFunction extends BiFunction<PersonEventRecord, Person, InstantView<Person>> {

    Class<? extends PersonEvent> supports();
}
