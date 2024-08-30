package com.restapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.restapi.config.Base64Validator;
import com.restapi.domain.dto.SheetMusicDto;
import com.restapi.domain.entities.SheetMusicEntity;
import com.restapi.domain.entities.SubscriptionEntity;
import com.restapi.exceptions.BadRequestException;
import com.restapi.mappers.Mapper;
import com.restapi.services.SheetMusicService;
import com.restapi.services.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Sheet music")
@RequestMapping("/api/v1/sheet-music")
@RestController
public class SheetMusicController {

  private SheetMusicService sheetMusicService;

  private SubscriptionService subscriptionService;

  private Mapper<SheetMusicEntity, SheetMusicDto> sheetMusicMapper;

  public SheetMusicController(SheetMusicService sheetMusicService,
                              Mapper<SheetMusicEntity, SheetMusicDto> sheetMusicMapper,
                              SubscriptionService subscriptionService) {
    this.sheetMusicService = sheetMusicService;
    this.subscriptionService = subscriptionService;
    this.sheetMusicMapper = sheetMusicMapper;
  }

  @GetMapping(path = "")
  public List<SheetMusicDto> findAll() {
    List<SheetMusicEntity> sheetMusics = sheetMusicService.findAll();
    return sheetMusics.stream()
            .map(sheetMusicMapper::mapTo)
            .map(sheetMusicDto -> {
              sheetMusicDto.add(
                      linkTo(methodOn(SheetMusicController.class)
                              .findById(sheetMusicDto.getId())).withSelfRel()
                              .withType("GET"));
              sheetMusicDto
                      .add(linkTo(methodOn(SheetMusicController.class)
                              .updateById(sheetMusicDto.getId(), null))
                              .withRel("update").withType("PUT"));
              sheetMusicDto.add(linkTo(methodOn(SheetMusicController.class)
                      .deleteById(sheetMusicDto.getId()))
                      .withRel("delete").withType("DELETE"));
              return sheetMusicDto;
            })
            .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SheetMusicDto> findById(@PathVariable("id") Long id) {

    SheetMusicEntity sheetMusicEntity = sheetMusicService.findById(id);
    SheetMusicDto sheetMusicDto = sheetMusicMapper.mapTo(sheetMusicEntity);

    sheetMusicDto.add(linkTo(methodOn(SheetMusicController.class)
            .findById(id)).withSelfRel().withType("GET"));
    sheetMusicDto.add(
            linkTo(methodOn(SheetMusicController.class)
                    .updateById(id, null)).withRel("update").withType("PUT"));
    sheetMusicDto
            .add(linkTo(methodOn(SheetMusicController.class)
                    .deleteById(id)).withRel("delete").withType("DELETE"));
    sheetMusicDto
            .add(linkTo(methodOn(SheetMusicController.class)
                    .findAll()).withRel("all sheet music").withType("GET"));

    return new ResponseEntity<>(sheetMusicDto, HttpStatus.OK);
  }

  @PostMapping(path = "")
  public ResponseEntity<SheetMusicDto> createSheetMusic(@RequestBody SheetMusicDto sheetMusic) {

    if (!(sheetMusic.getData() == null) && !Base64Validator.isBase64Encoded(sheetMusic.getData())) {
      throw new BadRequestException("Invalid base64 encoded data(file).");
    }

    SheetMusicEntity sheetMusicEntity = sheetMusicMapper.mapFrom(sheetMusic);
    SheetMusicEntity savedSheetMusicEntity = sheetMusicService.save(sheetMusicEntity);

    // Notify subscribers
    List<SubscriptionEntity> subscriptions = subscriptionService
            .findByInstrumentId(sheetMusicEntity.getInstrumentEntity().getId());
    for (SubscriptionEntity subscription : subscriptions) {
      notifySubscriber(subscription, savedSheetMusicEntity);
    }

    SheetMusicDto sheetMusicDto = sheetMusicMapper.mapTo(savedSheetMusicEntity);

    sheetMusicDto.add(linkTo(methodOn(SheetMusicController.class)
            .findById(sheetMusicDto.getId())).withSelfRel()
            .withType("GET"));
    sheetMusicDto.add(
            linkTo(methodOn(SheetMusicController.class)
                    .updateById(sheetMusicDto.getId(), null)).withRel("update")
                    .withType("PUT"));
    sheetMusicDto
            .add(linkTo(methodOn(SheetMusicController.class)
                    .deleteById(sheetMusicDto.getId())).withRel("delete")
                    .withType("DELETE"));
    sheetMusicDto
            .add(linkTo(methodOn(SheetMusicController.class)
                    .findAll()).withRel("all sheet music").withType("GET"));

    return new ResponseEntity<>(sheetMusicDto, HttpStatus.CREATED);

  }

  private void notifySubscriber(SubscriptionEntity subscription,
                                SheetMusicEntity sheetMusicEntity) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<SheetMusicEntity> request = new HttpEntity<>(sheetMusicEntity, headers);
    System.out.println("Notifying subscriber: " + subscription.getCallbackUrl());
    restTemplate.postForEntity(subscription.getCallbackUrl(), request, Void.class);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SheetMusicDto> updateById(@PathVariable("id") Long id,
                                                  @RequestBody SheetMusicDto sheetMusic) {

    if (!(sheetMusic.getData() == null) && !Base64Validator.isBase64Encoded(sheetMusic.getData())) {
      throw new BadRequestException("Invalid base64 encoded data(file).");
    }
    SheetMusicEntity sheetMusicEntity = sheetMusicMapper.mapFrom(sheetMusic);
    SheetMusicEntity updatedEntity = sheetMusicService.updateById(id, sheetMusicEntity);

    SheetMusicDto sheetMusicDto = sheetMusicMapper.mapTo(updatedEntity);

    sheetMusicDto.add(linkTo(methodOn(SheetMusicController.class)
            .findById(id)).withSelfRel().withType("GET"));
    sheetMusicDto
            .add(linkTo(methodOn(SheetMusicController.class)
                    .deleteById(id)).withRel("delete").withType("DELETE"));
    sheetMusicDto.add(
            linkTo(methodOn(SheetMusicController.class)
                    .findAll()).withRel("all sheet music").withType("POST"));

    return new ResponseEntity<>(sheetMusicDto, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<EntityModel<Object>> deleteById(@PathVariable("id") Long id) {
    sheetMusicService.deleteById(id);

    // Create a message
    Object message = new Object() {
      public String message = "Sheet music with id " + id + " has been deleted";
    };

    // Create an EntityModel to hold the message
    EntityModel<Object> entityModel = EntityModel.of(message);

    // Add HATEOAS links to the EntityModel
    entityModel
            .add(linkTo(methodOn(SheetMusicController.class)
                    .findAll()).withRel("all sheet music").withType("GET"));

    return ResponseEntity.ok(entityModel);
  }

}
