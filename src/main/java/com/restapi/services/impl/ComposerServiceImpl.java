package com.restapi.services.impl;

import com.restapi.domain.entities.ComposerEntity;
import com.restapi.domain.entities.SheetMusicEntity;
import com.restapi.exceptions.BadRequestException;
import com.restapi.exceptions.ResourceNotFoundException;
import com.restapi.repositories.ComposerRepository;
import com.restapi.repositories.SheetMusicRepository;
import com.restapi.services.ComposerService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class ComposerServiceImpl implements ComposerService {

  private ComposerRepository composerRepository;
  private SheetMusicRepository sheetMusicRepository;

  public ComposerServiceImpl(ComposerRepository composerRepository,
                             SheetMusicRepository sheetMusicRepository) {
    this.composerRepository = composerRepository;
    this.sheetMusicRepository = sheetMusicRepository;
  }

  @Override
  public ComposerEntity save(ComposerEntity newComposerEntity) {
    // Check if composer with that name already exsist
    ComposerEntity exsistingComposerEntity = composerRepository
            .findByName(newComposerEntity.getName());

    if (exsistingComposerEntity != null) {
      throw new BadRequestException("Composer with same name already exsists!");
    }

    if (newComposerEntity.getName() == null || newComposerEntity.getName().isEmpty()
            || newComposerEntity.getName() == "") {
      throw new BadRequestException("Name must not be empty.");
    } else if (!(newComposerEntity.getName() instanceof String)) {
      throw new BadRequestException("Name must be a string.");
    }
    return composerRepository.save(newComposerEntity);
  }

  @Override
  public List<ComposerEntity> findAll() {
    return StreamSupport.stream(composerRepository
                            .findAll()
                            .spliterator(),
                    false)
            .collect(Collectors.toList());
  }

  @Override
  public ComposerEntity findById(Long id) {
    return composerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Composer not found with id " + id));
  }

  @Override
  public ComposerEntity updateById(Long id, ComposerEntity newComposerEntity) {
    ComposerEntity composer = composerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Composer not found with id " + id));
    if (composer != null) {
      if (newComposerEntity.getName() != null) {
        ComposerEntity exsistingComposerEntity =
                composerRepository.findByName(newComposerEntity.getName());

        if (exsistingComposerEntity != null) {
          throw new BadRequestException("Composer with same title already exsists!");
        }
        composer.setName(newComposerEntity.getName());
      }
      if (newComposerEntity.getEra() != null) {
        composer.setEra(newComposerEntity.getEra());
      }
      return composerRepository.save(composer);
    }
    return null;
  }

  @Transactional
  public void deleteById(Long composerId) {
    ComposerEntity composer = composerRepository.findById(composerId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Composer not found with id "
                            + composerId));

    // Find all SheetMusic records associated with the composer
    List<SheetMusicEntity> musicSheets = sheetMusicRepository
            .findByComposerEntity_Id(composer.getId());

    // Set composerEntity to null for each SheetMusic record
    for (SheetMusicEntity sheet : musicSheets) {
      sheet.setComposerEntity(null);
      sheetMusicRepository.save(sheet);
    }

    // Now delete the composer
    composerRepository.delete(composer);
  }
}
