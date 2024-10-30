package org.tix.soa2.model;

import com.example.model.TicketType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "ticket_entity")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_entity_id")
    private CoordinatesEntity coordinates;

    @Column(name = "price")
    private Integer price;

    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    @OneToOne(orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "person_entity_id")
    private PersonEntity person;

}