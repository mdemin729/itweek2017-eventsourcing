package com.epam.eventsourcing.eventhandler;

import com.epam.eventsourcing.event.PersonEvent;

public interface PersonEventHandler {

    boolean supports(Class<? extends PersonEvent> clazz);

    void handle(PersonEvent event);
}
