package com.epam.eventsourcing;

public class EventPublicationException extends RuntimeException {

    public EventPublicationException(String message, Exception e) {
        super(message, e);
    }
}
