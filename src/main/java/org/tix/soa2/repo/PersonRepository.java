package org.tix.soa2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tix.soa2.model.PersonEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity,Long> {
}
