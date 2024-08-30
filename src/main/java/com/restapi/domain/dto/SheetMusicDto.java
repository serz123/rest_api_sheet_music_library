package com.restapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheetMusicDto extends RepresentationModel<SheetMusicDto> {

  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  private String title;

  private String data;

  private String fileLink;

  private String fileType;

  private ComposerDto composer;

  private InstrumentDto instrument;
}
