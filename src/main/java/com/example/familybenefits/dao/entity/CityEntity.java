package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Модель записи таблицы "cityEntity"
 */
@Entity
@Table(name = "city", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CityEntity extends ObjectEntity {

  /**
   * ID города
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Название города
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация города
   */
  @Nullable
  @Column(name = "info")
  private String info;

  /**
   * Множество учреждений города
   */
  @NonNull
  @ToString.Exclude
  @OneToMany(mappedBy = "cityEntity")
  private Set<InstitutionEntity> institutionEntitySet;

  /**
   * Множество пособий города
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany(mappedBy = "cityEntitySet")
  private Set<BenefitEntity> benefitEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID города
   */
  public CityEntity(@NonNull String id) {
    this.id = id;
  }
}
