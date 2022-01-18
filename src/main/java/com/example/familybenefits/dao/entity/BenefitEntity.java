package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

/**
 * Модель таблицы "benefit"
 */
@Entity
@Table(name = "benefit", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class BenefitEntity {

  /**
   * ID пособия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

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
   * Конструктор для создания модели по ID
   * @param id ID пособия
   */
  public BenefitEntity(@NonNull BigInteger id) {
    this.id = id;
  }

  /**
   * Множество городов пособия
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_cities", schema = "familybenefit",
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
      name = "benefits_institutions", schema = "familybenefit",
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
    name = "benefits_criterions", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_criterion"))
  private Set<CriterionEntity> criterionEntitySet;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    BenefitEntity benefitEntity = (BenefitEntity) o;
    return Objects.equals(id, benefitEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
