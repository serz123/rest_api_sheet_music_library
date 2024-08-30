package com.restapi.services.impl;

import com.restapi.domain.entities.ComposerEntity;
import com.restapi.domain.entities.InstrumentEntity;
import com.restapi.domain.entities.SheetMusicEntity;
import com.restapi.exceptions.BadRequestException;
import com.restapi.exceptions.ResourceNotFoundException;
import com.restapi.repositories.ComposerRepository;
import com.restapi.repositories.InstrumentRepository;
import com.restapi.repositories.SheetMusicRepository;
import com.restapi.services.SheetMusicService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class SheetMusicServiceImpl implements SheetMusicService {

  private SheetMusicRepository sheetMusicRepository;
  private ComposerRepository composerRepository;
  private InstrumentRepository instrumentRepository;
  private static final Set<String> validFileTypes = Set.of("pdf", "jpg", "png", "link");

  public SheetMusicServiceImpl(SheetMusicRepository sheetMusicRepository,
                               ComposerRepository composerRepository,
                               InstrumentRepository instrumentRepository) {
    this.sheetMusicRepository = sheetMusicRepository;
    this.composerRepository = composerRepository;
    this.instrumentRepository = instrumentRepository;
  }

  @Override
  public SheetMusicEntity save(SheetMusicEntity newSheetMusicEntity) {
    // Check if sheet music with that name already exsist
    SheetMusicEntity exsistingSheetMusicEntity = sheetMusicRepository
            .findByTitle(newSheetMusicEntity.getTitle());

    if (exsistingSheetMusicEntity != null) {
      throw new BadRequestException("Sheet music with same title already exsists!");
    }
    if (newSheetMusicEntity.getTitle() == null || newSheetMusicEntity.getTitle().isEmpty()
            || newSheetMusicEntity.getTitle() == "") {
      throw new BadRequestException("Title must not be empty.");
    } else if (!(newSheetMusicEntity.getTitle() instanceof String)) {
      throw new BadRequestException("Title must be a string.");
    }

    // Validate fileType
    String fileType = newSheetMusicEntity.getFileType();
    if (fileType == null || fileType.isEmpty()) {
      throw new BadRequestException("File type must not be empty.");
    }

    if (!validFileTypes.contains(fileType.toLowerCase())) {
      throw new BadRequestException("Invalid file type: " + fileType);
    }

    // Check if composer exists
    ComposerEntity composerEntity = composerRepository
            .findByName(newSheetMusicEntity.getComposerEntity().getName());
    if (composerEntity != null) {
      newSheetMusicEntity.setComposerEntity(composerEntity);
    }

    // Check if instrument exists
    InstrumentEntity instrumentEntity = instrumentRepository
            .findByName(newSheetMusicEntity.getInstrumentEntity().getName());
    if (instrumentEntity != null) {
      newSheetMusicEntity.setInstrumentEntity(instrumentEntity);
    }

    // sheetMusicEntity.setComposerEntity(sheetMusicEntity.getComposerEntity().getName());
    return sheetMusicRepository.save(newSheetMusicEntity);
  }

  @Override
  public List<SheetMusicEntity> findAll() {
    return StreamSupport.stream(sheetMusicRepository
                            .findAll()
                            .spliterator(),
                    false)
            .collect(Collectors.toList());
  }

  @Override
  public SheetMusicEntity findById(Long id) {
    return sheetMusicRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Sheet music not found with id " + id));
  }

  @Override
  public SheetMusicEntity updateById(Long id, SheetMusicEntity newSheetMusicEntity) {
    SheetMusicEntity entity = sheetMusicRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Sheet music not found with id " + id));

    // Update the entity with the new values if they are not null
    // Title
    if (newSheetMusicEntity.getTitle() != null || newSheetMusicEntity.getTitle() != "") {
      SheetMusicEntity exsistingSheetMusicEntity = sheetMusicRepository
              .findByTitle(newSheetMusicEntity.getTitle());

      if (exsistingSheetMusicEntity != null) {
        throw new BadRequestException("Sheet music with same title already exsists!");
      }
      entity.setTitle(newSheetMusicEntity.getTitle());
    }
    // Data
    if (newSheetMusicEntity.getData() != null) {
      entity.setData(newSheetMusicEntity.getData());
    }
    // File link
    if (newSheetMusicEntity.getFileLink() != null) {
      entity.setFileLink(newSheetMusicEntity.getFileLink());
    }

    // File type
    if (newSheetMusicEntity.getFileType() != null) {
      if (!validFileTypes.contains(newSheetMusicEntity.getFileType().toLowerCase())) {
        throw new BadRequestException("Invalid file type: " + newSheetMusicEntity.getFileType());
      }
      entity.setFileType(newSheetMusicEntity.getFileType());
    }

    // Composer
    if (newSheetMusicEntity.getComposerEntity() != null) {
      ComposerEntity exsistingComposerEntity = composerRepository
              .findByName(newSheetMusicEntity.getComposerEntity().getName());
      if (exsistingComposerEntity != null) {
        newSheetMusicEntity.setComposerEntity(exsistingComposerEntity);
      }
      entity.setComposerEntity(newSheetMusicEntity.getComposerEntity());
    }

    // Instrument
    if (newSheetMusicEntity.getInstrumentEntity() != null) {
      InstrumentEntity exsistingInstrumentEntity = instrumentRepository
              .findByName(newSheetMusicEntity.getInstrumentEntity().getName());
      if (exsistingInstrumentEntity != null) {
        newSheetMusicEntity.setInstrumentEntity(exsistingInstrumentEntity);
      }
      entity.setInstrumentEntity(newSheetMusicEntity.getInstrumentEntity());
    }
    return sheetMusicRepository.save(entity);
  }

  @Override
  public void deleteById(Long id) {
    if (!sheetMusicRepository.existsById(id)) {
      throw new ResourceNotFoundException("Sheet music not found with id " + id);
    }
    sheetMusicRepository.deleteById(id);
  }

}
