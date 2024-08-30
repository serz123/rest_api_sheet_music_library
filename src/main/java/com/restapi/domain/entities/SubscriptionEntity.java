package com.restapi.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"callbackUrl", "instrumentId"}))
public class SubscriptionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_id_seq")
  private Long id;

  @Column(nullable = false)
  private String callbackUrl;

  @Column(nullable = false)
  private Long instrumentId;

}
