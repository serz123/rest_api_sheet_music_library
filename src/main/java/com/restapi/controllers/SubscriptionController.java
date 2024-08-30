package com.restapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.restapi.domain.dto.SubscriptionDto;
import com.restapi.domain.entities.SubscriptionEntity;
import com.restapi.mappers.Mapper;
import com.restapi.services.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Webhooks")
@RequestMapping("/api/v1/subscriptions")
@RestController
public class SubscriptionController {

  private SubscriptionService subscriptionService;

  private Mapper<SubscriptionEntity, SubscriptionDto> subscriptionMapper;

  public SubscriptionController(SubscriptionService subscriptionService,
                                Mapper<SubscriptionEntity, SubscriptionDto> subscriptionMapper) {
    this.subscriptionService = subscriptionService;
    this.subscriptionMapper = subscriptionMapper;
  }

  @GetMapping(path = "/sheet-music")
  public List<SubscriptionDto> findAll() {
    List<SubscriptionEntity> subscriptions = subscriptionService.findAll();
    return subscriptions.stream()
            .map(subscriptionMapper::mapTo)
            .map(subscriptionDto -> {
              subscriptionDto.add(
                      linkTo(methodOn(SubscriptionController.class)
                              .findById(subscriptionDto.getId())).withSelfRel().withType("GET"));
              subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
                      .deleteById(subscriptionDto.getId()))
                      .withRel("unsubscribe").withType("DELETE"));
              return subscriptionDto;
            })
            .collect(Collectors.toList());
  }

  @GetMapping("/sheet-music/{id}")
  public ResponseEntity<SubscriptionDto> findById(@PathVariable("id") Long id) {
    SubscriptionEntity subscriptionEntity = subscriptionService.findById(id);
    SubscriptionDto subscriptionDto = subscriptionMapper.mapTo(subscriptionEntity);

    subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
            .findById(id)).withSelfRel().withType("GET"));
    subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
            .deleteById(id)).withRel("unsubscribe").withType("DELETE"));
    subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
            .findAll()).withRel("all subscriptions").withType("GET"));

    return new ResponseEntity<>(subscriptionDto, HttpStatus.OK);
  }

  @PostMapping(path = "/sheet-music")
  public ResponseEntity<SubscriptionDto> subscribe(@RequestBody SubscriptionDto subscription) {
    SubscriptionEntity subscriptionEntity = subscriptionMapper.mapFrom(subscription);

    SubscriptionEntity savedSubscriptionEntity = subscriptionService.save(subscriptionEntity);

    SubscriptionDto subscriptionDto = subscriptionMapper.mapTo(savedSubscriptionEntity);

    // Add HATEOAS links
    subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
            .findById(subscriptionDto.getId())).withSelfRel().withType("GET"));
    subscriptionDto
            .add(linkTo(methodOn(SubscriptionController.class)
                    .deleteById(subscriptionDto.getId()))
                    .withRel("unsubscribe").withType("DELETE"));
    subscriptionDto.add(linkTo(methodOn(SubscriptionController.class)
            .findAll()).withRel("all subscriptions").withType("GET"));

    return new ResponseEntity<>(subscriptionDto, HttpStatus.CREATED);
  }

  @DeleteMapping("/sheet-music/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable Long id) {
    subscriptionService.deleteById(id);

    // Create a message
    Object message = new Object() {
      public String message = "Sheet music with id " + id + " has been deleted";
    };

    // Create an EntityModel to hold the message
    EntityModel<Object> entityModel = EntityModel.of(message);

    // Add HATEOAS links to the EntityModel
    entityModel.add(linkTo(methodOn(SubscriptionController.class)
            .findAll()).withRel("all subscriptions").withType("GET"));

    return ResponseEntity.ok(entityModel);
  }
}
