package com.restapi.services;

import com.restapi.domain.dto.JwtAuthenticationResponse;
import com.restapi.domain.dto.LogInRequest;
import com.restapi.domain.dto.RegisterRequest;
import com.restapi.domain.entities.Role;
import com.restapi.domain.entities.User;
import com.restapi.exceptions.BadRequestException;
import com.restapi.exceptions.InvalidCredentialsException;
import com.restapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  // Return 200 OK without JWT token
  public void register(RegisterRequest request) {
    var exsistingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
    if (exsistingUser != null) {
      throw new BadRequestException("User with email " + request.getEmail() + " already exists!");
    }

    if (request.getFirstName() == null
            || request.getLastName() == null || request.getEmail() == null
            || request.getPassword() == null) {
      throw new BadRequestException("First name, last name, email and password are mandatory!");
    } else if (!request.getEmail().matches(".+@.+\\..+")) {
      throw new BadRequestException("Email should be valid!");
    }

    var user = User
            .builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_USER)
            .build();
    userService.save(user);
  }

  public JwtAuthenticationResponse login(LogInRequest request) {
    try {
      // Attempt authentication
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    } catch (BadCredentialsException e) {
      throw new InvalidCredentialsException("Invalid email or password!");
    }

    // Check if user exists
    userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password!"));

    // Generate JWT token
    var jwt = jwtService.generateToken(request.getEmail());
    return JwtAuthenticationResponse.builder().token(jwt).build();
  }
}
