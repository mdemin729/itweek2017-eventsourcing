package com.epam.eventsourcing.event;

import com.epam.eventsourcing.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonCreated implements PersonEvent {
    private UUID id;
    private String name;
    private Gender gender;
    private String email;
}
