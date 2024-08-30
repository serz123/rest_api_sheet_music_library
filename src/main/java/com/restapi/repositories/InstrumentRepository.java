package com.restapi.repositories;

import com.restapi.domain.entities.InstrumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstrumentRepository extends CrudRepository<InstrumentEntity, Long> {
  InstrumentEntity findByName(String name);
}