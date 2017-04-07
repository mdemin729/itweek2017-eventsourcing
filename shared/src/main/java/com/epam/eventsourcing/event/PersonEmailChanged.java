package com.epam.eventsourcing.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonEmailChanged implements PersonEvent {
    private UUID id;
    private String email;
}
