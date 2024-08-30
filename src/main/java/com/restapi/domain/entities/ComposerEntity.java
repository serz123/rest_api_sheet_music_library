package com.restapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "composers")
public class ComposerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "composer_id_seq")
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  private String era;
}
