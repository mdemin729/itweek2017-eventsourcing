package com.epam.eventsourcing.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PersonCreated.class, name = "PersonCreated"),
        @JsonSubTypes.Type(value = PersonNameChanged.class, name = "PersonNameChanged"),
        @JsonSubTypes.Type(value = PersonEmailChanged.class, name = "PersonEmailChanged")
})
public interface PersonEvent {

    UUID getId();
}
