package gait.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import gait.api.models.GaitMessage;

public interface GaitMessageRepository extends JpaRepository<GaitMessage, Integer> {

}
