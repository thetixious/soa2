package org.tix.soa2.model;

import com.example.model.TicketType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ticket_entity")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "coordinates_entity_id")
    private CoordinatesEntity coordinates;

    @Column(name = "price")
    private Integer price;

    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "person_entity_id")
    private PersonEntity person;

}