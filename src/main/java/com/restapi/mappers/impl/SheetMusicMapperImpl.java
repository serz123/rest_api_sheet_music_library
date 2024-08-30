package com.restapi.mappers.impl;

import com.restapi.domain.dto.SheetMusicDto;
import com.restapi.domain.entities.SheetMusicEntity;
import com.restapi.mappers.Mapper;
import java.util.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SheetMusicMapperImpl implements Mapper<SheetMusicEntity, SheetMusicDto> {

  private ModelMapper modelMapper;
  private ComposerMapperImpl composerMapper;
  private InstrumentMapperImpl instrumentMapper;

  public SheetMusicMapperImpl(ModelMapper modelMapper,
                              ComposerMapperImpl composerMapper,
                              InstrumentMapperImpl instrumentMapper) {
    this.modelMapper = modelMapper;
    this.composerMapper = composerMapper;
    this.instrumentMapper = instrumentMapper;
  }

  @Override
  public SheetMusicDto mapTo(SheetMusicEntity musicSheetEntity) {
    SheetMusicDto musicSheetDto = modelMapper.map(musicSheetEntity, SheetMusicDto.class);
    if (musicSheetEntity.getData() != null) {
      String base64File = Base64.getEncoder().encodeToString(musicSheetEntity.getData());
      musicSheetDto.setData(base64File);
    } else {
      musicSheetDto.setData("");
    }
    // If composer and instrument are not null, map them to the corresponding DTOs
    if (musicSheetEntity.getComposerEntity() != null) {
      musicSheetDto.setComposer(composerMapper.mapTo(musicSheetEntity.getComposerEntity()));
    }
    if (musicSheetEntity.getInstrumentEntity() != null) {
      musicSheetDto.setInstrument(instrumentMapper.mapTo(musicSheetEntity.getInstrumentEntity()));
    }
    return musicSheetDto;
  }

  @Override
  public SheetMusicEntity mapFrom(SheetMusicDto musicSheetDto) {
    SheetMusicEntity musicSheetEntity = modelMapper.map(musicSheetDto, SheetMusicEntity.class);
    if (musicSheetDto.getData() != null) {
      byte[] fileBytes = Base64.getDecoder().decode(musicSheetDto.getData());
      musicSheetEntity.setData(fileBytes);
    } else {
      musicSheetEntity.setData(null);
    }

    // If composer and instrument are not null, map them to the corresponding entities
    if (musicSheetDto.getComposer() != null) {
      musicSheetEntity.setComposerEntity(composerMapper.mapFrom(musicSheetDto.getComposer()));
    }
    if (musicSheetDto.getInstrument() != null) {
      musicSheetEntity.setInstrumentEntity(instrumentMapper.mapFrom(musicSheetDto.getInstrument()));
    }
    return musicSheetEntity;
  }
}
