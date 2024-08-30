package com.restapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.restapi.domain.dto.ComposerDto;
import com.restapi.domain.entities.ComposerEntity;
import com.restapi.mappers.Mapper;
import com.restapi.services.ComposerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Composers")
@RequestMapping("/api/v1/composers")
@RestController
public class ComposerController {
  private ComposerService composerService;

  private Mapper<ComposerEntity, ComposerDto> composerMapper;

  public ComposerController(ComposerService composerService,
                            Mapper<ComposerEntity, ComposerDto> composerMapper) {
    this.composerService = composerService;
    this.composerMapper = composerMapper;
  }

  @GetMapping(path = "")
  public List<ComposerDto> findAll() {
    List<ComposerEntity> composers = composerService.findAll();
    return composers.stream()
            .map(composerMapper::mapTo)
            .map(composerDto -> {
              composerDto.add(
                      linkTo(methodOn(ComposerController.class)
                              .findById(composerDto.getId()))
                              .withSelfRel().withType("GET"));
              composerDto
                      .add(linkTo(methodOn(ComposerController.class)
                              .updateById(composerDto.getId(), null))
                              .withRel("update").withType("PUT"));
              composerDto.add(linkTo(methodOn(ComposerController.class)
                      .deleteById(composerDto.getId()))
                      .withRel("delete").withType("DELETE"));
              return composerDto;
            })
            .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ComposerDto> findById(@PathVariable("id") Long id) {
    ComposerEntity composerEntity = composerService.findById(id);
    ComposerDto composerDto = composerMapper.mapTo(composerEntity);

    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findById(id)).withSelfRel().withType("GET"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .updateById(id, null)).withRel("update").withType("PUT"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .deleteById(id)).withRel("delete").withType("DELETE"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findAll()).withRel("all composers").withType("GET"));

    return new ResponseEntity<>(composerDto, HttpStatus.OK);
  }

  @PostMapping(path = "")
  public ResponseEntity<ComposerDto> createComposer(@RequestBody ComposerDto composer) {

    ComposerEntity composerEntity = composerMapper.mapFrom(composer);
    ComposerEntity savedComposerEntity = composerService.save(composerEntity);

    ComposerDto composerDto = composerMapper.mapTo(savedComposerEntity);

    // Add HATEOAS links
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findById(composerDto.getId())).withSelfRel().withType("GET"));
    composerDto.add(
            linkTo(methodOn(ComposerController.class)
                    .updateById(composerDto.getId(), null)).withRel("update").withType("PUT"));
    composerDto
            .add(linkTo(methodOn(ComposerController.class)
                    .deleteById(composerDto.getId())).withRel("delete").withType("DELETE"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findAll()).withRel("all composers").withType("GET"));

    return new ResponseEntity<>(composerDto, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ComposerDto> updateById(@PathVariable("id") Long id,
                                                @RequestBody ComposerDto composer) {
    ComposerEntity composerEntity = composerMapper.mapFrom(composer);
    ComposerEntity updatedEntity = composerService.updateById(id, composerEntity);

    ComposerDto composerDto = composerMapper.mapTo(updatedEntity);

    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findById(id)).withSelfRel().withType("GET"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .deleteById(id)).withRel("delete").withType("DELETE"));
    composerDto.add(linkTo(methodOn(ComposerController.class)
            .findAll()).withRel("all composers").withType("POST"));

    return new ResponseEntity<>(composerDto, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable("id") Long id) {
    composerService.deleteById(id);
    // Create a message
    Object message = new Object() {
      public String message = "Sheet music with id " + id + " has been deleted";
    };

    // Create an EntityModel to hold the message
    EntityModel<Object> entityModel = EntityModel.of(message);

    // Add HATEOAS links to the EntityModel
    entityModel.add(linkTo(methodOn(ComposerController.class)
            .findAll()).withRel("all composers").withType("GET"));

    return ResponseEntity.ok(entityModel);
  }
}
