package com.restapi.mappers.impl;

import com.restapi.domain.dto.SubscriptionDto;
import com.restapi.domain.entities.SubscriptionEntity;
import com.restapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapperImpl implements Mapper<SubscriptionEntity, SubscriptionDto> {

  private ModelMapper modelMapper;

  public SubscriptionMapperImpl(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public SubscriptionDto mapTo(SubscriptionEntity musicSheetEntity) {
    SubscriptionDto musicSheetDto = modelMapper.map(musicSheetEntity, SubscriptionDto.class);
    return musicSheetDto;
  }

  @Override
  public SubscriptionEntity mapFrom(SubscriptionDto musicSheetDto) {
    SubscriptionEntity musicSheetEntity = modelMapper.map(musicSheetDto, SubscriptionEntity.class);
    return musicSheetEntity;
  }
}
