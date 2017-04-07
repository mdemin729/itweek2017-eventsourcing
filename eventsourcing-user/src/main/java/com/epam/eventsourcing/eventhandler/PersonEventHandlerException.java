package com.epam.eventsourcing.eventhandler;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PersonEventHandlerException extends RuntimeException {
    public PersonEventHandlerException(String message, JsonProcessingException e) {
    }
}
