package com.epam.eventsourcing.dto;

import com.epam.eventsourcing.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePerson {
    @NotEmpty
    private String name;
    @NotNull
    private Gender gender;
    @Email
    private String email;
}
