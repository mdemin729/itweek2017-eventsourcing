package com.epam.eventsourcing.jpa;

import com.epam.eventsourcing.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Person {

    @Id
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Email
    private String email;

    @CreationTimestamp
    private Date created;
    @UpdateTimestamp
    private Date updated;
}
