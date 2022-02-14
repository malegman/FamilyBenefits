package com.example.familybenefits.dto.entity;

import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель записи таблицы "benefit"
 */
@Entity
@Table(name = "benefit", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class BenefitEntity extends ObjectEntity {

  /**
   * ID пособия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Название пособия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация пособия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  /**
   * Документы пособия
   */
  @Nullable
  @Column(name = "documents")
  private String documents;

  /**
   * Множество городов пособия
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_cities", schema = "family_benefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_city"))
  private Set<CityEntity> cityEntitySet;

  /**
   * Множество учреждений пособия
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
      name = "benefits_institutions", schema = "family_benefit",
      joinColumns = @JoinColumn(name = "id_benefit"),
      inverseJoinColumns = @JoinColumn(name = "id_institution"))
  private Set<InstitutionEntity> institutionEntitySet;

  /**
   * Множество критерий пособия
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_criteria", schema = "family_benefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_criterion"))
  private Set<CriterionEntity> criterionEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID пособия
   */
  public BenefitEntity(@NonNull String id) {
    this.id = id;
  }
}
