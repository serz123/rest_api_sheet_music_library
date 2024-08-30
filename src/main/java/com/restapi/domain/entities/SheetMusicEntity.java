package com.restapi.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "musicSheets")
public class SheetMusicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "musicSheet_id_seq")
  private Long id;

  @Column(unique = true, nullable = false)
  private String title;

  private byte[] data;

  private String fileLink;

  @Column(nullable = false)
  @Pattern(regexp = "(?i)PDF|JPG|PNG|LINK")
  private String fileType;

  @Getter
  @Setter
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "composer_id")
  private ComposerEntity composerEntity;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "instrument_id")
  private InstrumentEntity instrumentEntity;

}
