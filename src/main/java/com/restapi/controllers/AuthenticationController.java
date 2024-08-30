package com.restapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.restapi.domain.dto.JwtAuthenticationResponse;
import com.restapi.domain.dto.LogInRequest;
import com.restapi.domain.dto.RegisterRequest;
import com.restapi.exceptions.InvalidCredentialsException;
import com.restapi.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
    authenticationService.register(request);

    Object message = new Object() {
      public String message = "User registered successfully";
    };

    // Create an EntityModel to hold the message
    EntityModel<Object> entityModel = EntityModel.of(message);

    // Add HATEOAS links to the EntityModel
    entityModel.add(linkTo(methodOn(AuthenticationController.class)
            .login(null)).withRel("login").withType("POST"));

    return ResponseEntity.ok(entityModel);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LogInRequest request) {
    JwtAuthenticationResponse response = authenticationService.login(request);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }

  /*
   * In case logout is implemented in the backend add hateos to login
   *
   * @PostMapping("/login")
   * public ResponseEntity<EntityModel<JwtAuthenticationResponse>>
   * login(@RequestBody LogInRequest request) {
   * JwtAuthenticationResponse RestApiResponse =
   * RestApienticationService.login(request);
   *
   * EntityModel<JwtAuthenticationResponse> entityModel =
   * EntityModel.of(RestApiResponse);
   *
   * entityModel.add(linkTo(methodOn(AuthenticationController.class).login(null)).
   * withRel("login").withType("POST"));
   *
   * return ResponseEntity.status(HttpStatus.OK).body(entityModel);
   * }
   */

  // Logout should be handled by the client side
}
