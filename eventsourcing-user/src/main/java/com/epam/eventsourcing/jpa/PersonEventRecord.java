package com.epam.eventsourcing.jpa;

import com.epam.eventsourcing.event.PersonEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(columnList = "aggregateId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonEventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private Date timestamp;
    private UUID aggregateId;
    @Column(length = 4096, columnDefinition = "VARCHAR")
    @Type(type = "PersonEvent")
    private PersonEvent data;
}
