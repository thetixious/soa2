package org.tix.soa2.model;

import com.example.model.ColorE;
import com.example.model.ColorH;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Calendar;

@Getter
@Setter
@Entity
@Table(name = "person_entity")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private ColorE colorE;

    @Enumerated(EnumType.STRING)
    private ColorH colorH;

}