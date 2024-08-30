package com.restapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComposerDto extends RepresentationModel<ComposerDto> {

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  private String name;

  private String era;

}
