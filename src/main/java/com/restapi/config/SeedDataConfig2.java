package com.restapi.config;

import com.restapi.domain.entities.Role;
import com.restapi.domain.entities.User;
import com.restapi.repositories.UserRepository;
import com.restapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig2 implements CommandLineRunner {

  @Value("${admin.firstName}")
  private String adminFirstName;

  @Value("${admin.lastName}")
  private String adminLastName;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminPassword;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Override
  public void run(String... args) throws Exception {

    if (userRepository.count() == 0) {

      User admin = User
              .builder()
              .firstName(adminFirstName)
              .lastName(adminLastName)
              .email(adminEmail)
              .password(passwordEncoder.encode(adminPassword))
              .role(Role.ROLE_ADMIN)
              .build();

      userService.save(admin);
      log.debug("created ADMIN user - {}", admin);
    }
  }

}