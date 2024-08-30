package com.restapi.services;

import com.restapi.domain.entities.ComposerEntity;
import java.util.List;

public interface ComposerService {
  ComposerEntity save(ComposerEntity composerEntity);

  List<ComposerEntity> findAll();

  ComposerEntity findById(Long id);

  ComposerEntity updateById(Long id, ComposerEntity composerEntity);

  void deleteById(Long id);
}