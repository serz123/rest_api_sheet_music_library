package com.restapi.mappers.impl;

import com.restapi.domain.dto.InstrumentDto;
import com.restapi.domain.entities.InstrumentEntity;
import com.restapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class InstrumentMapperImpl implements Mapper<InstrumentEntity, InstrumentDto> {

  private ModelMapper modelMapper;

  public InstrumentMapperImpl(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public InstrumentDto mapTo(InstrumentEntity instrumentEntity) {
    return modelMapper.map(instrumentEntity, InstrumentDto.class);
  }

  @Override
  public InstrumentEntity mapFrom(InstrumentDto instrumentDto) {
    return modelMapper.map(instrumentDto, InstrumentEntity.class);
  }
}
