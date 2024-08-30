package com.restapi.services;

import com.restapi.domain.entities.InstrumentEntity;
import java.util.List;

public interface InstrumentService {
  List<InstrumentEntity> findAll();

  InstrumentEntity findById(Long id);
}
