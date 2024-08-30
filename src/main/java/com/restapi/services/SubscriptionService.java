package com.restapi.services;

import com.restapi.domain.entities.SubscriptionEntity;
import java.util.List;

public interface SubscriptionService {
  SubscriptionEntity save(SubscriptionEntity subscriptionEntity);

  List<SubscriptionEntity> findAll();

  SubscriptionEntity findById(Long id);

  List<SubscriptionEntity> findByInstrumentId(Long instrumentId);

  void deleteById(Long id);
}
