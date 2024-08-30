package com.restapi.repositories;

import com.restapi.domain.entities.SheetMusicEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetMusicRepository extends CrudRepository<SheetMusicEntity, Long> {
  List<SheetMusicEntity> findByComposerEntity_Id(Long composerId);

  SheetMusicEntity findByTitle(String title);
}
