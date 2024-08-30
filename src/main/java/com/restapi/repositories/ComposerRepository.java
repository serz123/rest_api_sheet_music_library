package com.restapi.repositories;

import com.restapi.domain.entities.ComposerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComposerRepository extends CrudRepository<ComposerEntity, Long> {
  ComposerEntity findByName(String name);
}