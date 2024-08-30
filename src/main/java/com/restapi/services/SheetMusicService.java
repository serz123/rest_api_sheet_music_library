package com.restapi.services;

import com.restapi.domain.entities.SheetMusicEntity;
import java.util.List;

public interface SheetMusicService {
  SheetMusicEntity save(SheetMusicEntity musicSheetEntity);

  List<SheetMusicEntity> findAll();

  SheetMusicEntity findById(Long id);

  SheetMusicEntity updateById(Long id, SheetMusicEntity newSheetMusicEntity);

  void deleteById(Long id);
}