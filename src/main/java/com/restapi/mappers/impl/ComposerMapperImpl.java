package com.restapi.mappers.impl;

import com.restapi.domain.dto.ComposerDto;
import com.restapi.domain.entities.ComposerEntity;
import com.restapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ComposerMapperImpl implements Mapper<ComposerEntity, ComposerDto> {

  private ModelMapper modelMapper;

  public ComposerMapperImpl(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public ComposerDto mapTo(ComposerEntity composerEntity) {
    return modelMapper.map(composerEntity, ComposerDto.class);
  }

  @Override
  public ComposerEntity mapFrom(ComposerDto composerDto) {
    return modelMapper.map(composerDto, ComposerEntity.class);
  }
}
