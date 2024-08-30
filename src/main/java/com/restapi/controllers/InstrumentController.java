package com.restapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.restapi.domain.dto.InstrumentDto;
import com.restapi.domain.entities.InstrumentEntity;
import com.restapi.mappers.Mapper;
import com.restapi.services.InstrumentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Instruments")
@RequestMapping("/api/v1/instruments")
@RestController
public class InstrumentController {

    private InstrumentService instrumentService;

    private Mapper<InstrumentEntity, InstrumentDto> instrumentMapper;

    public InstrumentController(InstrumentService instrumentService, Mapper<InstrumentEntity, InstrumentDto> instrumentMapper) {
        this.instrumentService = instrumentService;
        this.instrumentMapper = instrumentMapper;
    }

    @GetMapping(path = "")
    public List<InstrumentDto> findAll() {
        List<InstrumentEntity> instruments = instrumentService.findAll();
        return instruments.stream()
                .map(instrumentMapper::mapTo)
                .map(instrumentDto -> {
                    instrumentDto.add(
                            linkTo(methodOn(InstrumentController.class).findById(instrumentDto.getId())).withSelfRel().withType("GET"));
                    return instrumentDto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentDto> findById(@PathVariable("id") Long id) {
        InstrumentEntity instrumentEntity = instrumentService.findById(id);
        InstrumentDto instrumentDto = instrumentMapper.mapTo(instrumentEntity);

        instrumentDto.add(linkTo(methodOn(InstrumentController.class).findById(id)).withSelfRel().withType("GET"));
        instrumentDto.add(linkTo(methodOn(InstrumentController.class).findAll()).withRel("all instruments").withType("GET"));

        return new ResponseEntity<>(instrumentDto, HttpStatus.OK);
    }
}


    // TODO: Det betyder även att om ni har hårdkodad ett username för registrering
    // då kommer testet inte fungera om man kör om det fler gånger.
    // TODO: Samma gäller autentiserings token - spara det i ett variabel på
    // kollektionsnivå så kan man använda det i andra tester
    // TODO: Test should affect test database
    // TODO: Error handling and postman tests for error handling
    // TODO: Readme how to test it
    // TODO: Deploy
    // TODO: Add more tests in postman

