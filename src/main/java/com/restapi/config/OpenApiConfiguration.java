package com.restapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.HashMap;
import java.util.Map;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI openAPI() {
    // Define API information
    Info info = new Info()
            .title("Sheet Music Library API")
            .version("1.0")
            .description("This API exposes endpoints to manage sheet music library");

    // Define security scheme
    final String securitySchemeName = "bearerAuth";
    SecurityScheme securityScheme = new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

    // Create and return OpenAPI configuration
    return new OpenAPI()
            .info(info)
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Bean
  public OpenApiCustomizer openApiCustomiser() {
    return openApi -> {
      // Remove all schemas
      openApi.getComponents().setSchemas(new HashMap<>());

      // Define schemas
      @SuppressWarnings("rawtypes")
      Map<String, Schema> schemas = new HashMap<>();

      schemas.put("SheetMusicDto",
              new Schema<>()
                      .title("SheetMusicDto")
                      .description("Sheet Music Data Transfer Object")
                      .addProperties("id", new IntegerSchema()
                              .description("ID of the sheet music"))
                      .addProperties("title", new StringSchema()
                              .description("Title of the sheet music"))
                      .addProperties("data", new Schema<>()
                              .type("array")
                              .items(new StringSchema())
                              .description("File content"))
                      .addProperties("fileLink", new StringSchema()
                              .description("Link to the file"))
                      .addProperties("fileType", new StringSchema()
                              .description("Type of the file"))
                      .addProperties("composer", new Schema<>()
                              .title("ComposerDto")
                              .description("Composer Data Transfer Object")
                              .addProperties("name", new StringSchema()
                                      .description("Name of the composer"))
                              .addProperties("era", new StringSchema()
                                      .description("Era of the composer")))
                      .addProperties("instrument", new Schema<>()
                              .title("InstrumentDto")
                              .description("Instrument Data Transfer Object")
                              .addProperties("name", new StringSchema()
                                      .description("Name of the instrument"))
                              .addProperties("family", new StringSchema()
                                      .description("Family of the instrument"))));

      schemas.put("ComposerDto",
              new Schema<>()
                      .title("ComposerDto")
                      .description("Composer Data Transfer Object")
                      .addProperties("name", new StringSchema()
                              .description("Name of the composer"))
                      .addProperties("era", new StringSchema()
                              .description("Era of the composer")));

      schemas.put("InstrumentDto",
              new Schema<>()
                      .title("InstrumentDto")
                      .description("Instrument Data Transfer Object")
                      .addProperties("name", new StringSchema()
                              .description("Name of the instrument"))
                      .addProperties("family", new StringSchema()
                              .description("Family of the instrument")));

      schemas.put("RegisterRequest",
              new Schema<>()
                      .title("RegisterRequest")
                      .description("Register Request Data Transfer Object")
                      .addProperties("firstName", new StringSchema()
                              .description("First name of the user"))
                      .addProperties("lastName", new StringSchema()
                              .description("Last name of the user"))
                      .addProperties("email", new StringSchema()
                              .description("Email address of the user"))
                      .addProperties("password", new StringSchema()
                              .description("Password for the user")));

      schemas.put("LogInRequest",
              new Schema<>()
                      .title("LogInRequest")
                      .description("Log In Request Data Transfer Object")
                      .addProperties("email", new StringSchema()
                              .description("Email address of the user"))
                      .addProperties("password", new StringSchema()
                              .description("Password for the user")));

      schemas.put("SubscriptionDto",
              new Schema<>()
                      .title("SubscriptionDto")
                      .description("Subscription Data Transfer Object")
                      .addProperties("callbackUrl", new StringSchema()
                              .description("Callback URL for the subscription"))
                      .addProperties("instrumentId", new IntegerSchema()
                              .description("ID of the instrument associated with the subscription")));

      schemas.put("JwtAuthenticationResponse",
              new Schema<>()
                      .title("JwtAuthenticationResponse")
                      .description("JWT Authentication Response")
                      .addProperties("token", new StringSchema()
                              .description("JWT token")));

      schemas.put("EntityModelObject",
              new Schema<>()
                      .title("EntityModelObject")
                      .description("Generic Entity Model from Spring HATEOAS")
                      .addProperties("_links", new Schema<>()
                              .description("Links related to the entity")));

      openApi.getComponents().setSchemas(schemas);
    };
  }
}

