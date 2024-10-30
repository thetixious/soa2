package org.tix.soa2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tix.soa2.model.CoordinatesEntity;
@Repository
public interface CoordinatesRepo extends JpaRepository<CoordinatesEntity,Long> {
}
