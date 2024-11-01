package org.tix.soa2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tix.soa2.model.TicketEntity;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long>, JpaSpecificationExecutor<TicketEntity> {

    Optional<TicketEntity> findByPrice(Integer price);
    Optional<Integer> countAllByPrice(Integer price);

    @Modifying
    @Query(value = "UPDATE TicketEntity t SET t.name = :#{#new_ticket.name} , t.price = :#{#new_ticket.price}," +
            " t.type = :#{#new_ticket.type}, t.comment = :#{#new_ticket.comment}," +
            " t.creationDate= :#{#new_ticket.creationDate}, t.coordinates = :#{#new_ticket.coordinates}," +
            "t.person = :#{#new_ticket.person} WHERE t.id = :id")
    void updateTicketEntity(@Param("id") Long id, @Param("new_ticket") TicketEntity newTicketEntity);

}
