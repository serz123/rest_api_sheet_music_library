package com.restapi.services.impl;

import com.restapi.domain.entities.SubscriptionEntity;
import com.restapi.exceptions.BadRequestException;
import com.restapi.exceptions.ResourceNotFoundException;
import com.restapi.repositories.ComposerRepository;
import com.restapi.repositories.InstrumentRepository;
import com.restapi.repositories.SubscriptionRepository;
import com.restapi.services.SubscriptionService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  private SubscriptionRepository subscriptionRepository;
  private InstrumentRepository instrumentRepository;

  public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                 ComposerRepository composerRepository,
                                 InstrumentRepository instrumentRepository) {
    this.subscriptionRepository = subscriptionRepository;
    this.instrumentRepository = instrumentRepository;
  }

  @Override
  public SubscriptionEntity save(SubscriptionEntity newSubscriptionEntity) {
    // Validate callbackUrl
    String callbackUrl = newSubscriptionEntity.getCallbackUrl();
    if (callbackUrl == null || callbackUrl.isEmpty()) {
      throw new BadRequestException("CallbackUrl must not be empty.");
    }

    // Check if the callbackUrl is a valid URI
    try {
      URI uri = new URI(callbackUrl);
      String scheme = uri.getScheme();
      if (scheme == null || !(scheme.equalsIgnoreCase("http")
              || scheme.equalsIgnoreCase("https"))) {
        throw new BadRequestException("CallbackUrl must be a valid HTTP/HTTPS URL.");
      }
    } catch (URISyntaxException e) {
      throw new BadRequestException("CallbackUrl is not a valid URL.");
    }

    // Check if instrumentId is valid
    if (newSubscriptionEntity.getInstrumentId() == null) {
      throw new BadRequestException("InstrumentId must not be empty.");
    }

    // Check if instrument exists
    instrumentRepository.findById(newSubscriptionEntity.getInstrumentId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Instrument not found with id "
                    + newSubscriptionEntity.getInstrumentId()));

    // Check if subscription with the same callbackUrl and instrumentId already exists
    List<SubscriptionEntity> existingSubscriptions = subscriptionRepository
            .findByInstrumentId(newSubscriptionEntity.getInstrumentId());
    for (SubscriptionEntity subscriptionEntity : existingSubscriptions) {
      if (subscriptionEntity.getCallbackUrl()
              .equalsIgnoreCase(newSubscriptionEntity.getCallbackUrl())) {
        throw new
                BadRequestException("Subscription with the same callbackUrl and instrumentId already exists!");
      }
    }

    return subscriptionRepository.save(newSubscriptionEntity);
  }

  @Override
  public List<SubscriptionEntity> findAll() {
    return StreamSupport.stream(subscriptionRepository
                            .findAll()
                            .spliterator(),
                    false)
            .collect(Collectors.toList());
  }

  @Override
  public SubscriptionEntity findById(Long id) {
    return subscriptionRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Sheet music subscription not found with id "
                            + id));
  }

  @Override
  public List<SubscriptionEntity> findByInstrumentId(Long instrumentId) {

    List<SubscriptionEntity> allSubscriptionsEntities = StreamSupport.stream(subscriptionRepository
                            .findAll()
                            .spliterator(),
                    false)
            .collect(Collectors.toList());

    // Return subscriptions that have an instrumentId equal to the one provided
    return allSubscriptionsEntities.stream()
            .filter(subscriptionEntity -> subscriptionEntity.getInstrumentId().equals(instrumentId))
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    if (!subscriptionRepository.existsById(id)) {
      throw new ResourceNotFoundException("Sheet music subscription not found with id " + id);
    }
    subscriptionRepository.deleteById(id);
  }

}
