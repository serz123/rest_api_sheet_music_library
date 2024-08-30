package com.restapi.repositories;

import com.restapi.domain.entities.SubscriptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
  List<SubscriptionEntity> findByInstrumentId(Long instrumentId);
}
