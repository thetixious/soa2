package org.tix.soa2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tix.soa2.model.TicketEntity;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {




}
