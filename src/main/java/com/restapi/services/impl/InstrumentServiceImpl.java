package com.restapi.services.impl;

import com.restapi.domain.entities.InstrumentEntity;
import com.restapi.exceptions.ResourceNotFoundException;
import com.restapi.repositories.InstrumentRepository;
import com.restapi.services.InstrumentService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class InstrumentServiceImpl implements InstrumentService {

  private InstrumentRepository instrumentRepository;

  public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
    this.instrumentRepository = instrumentRepository;
  }

  @Override
  public List<InstrumentEntity> findAll() {
    return StreamSupport.stream(instrumentRepository
                            .findAll()
                            .spliterator(),
                    false)
            .collect(Collectors.toList());
  }

  @Override
  public InstrumentEntity findById(Long id) {
    return instrumentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instrument not found with id " + id));
  }
}
