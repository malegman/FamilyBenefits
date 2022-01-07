package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * Модель таблицы "benefit"
 */
@Entity
@Table(name = "benefit", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Benefit {

  /**
   * ID пособия
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название пособия
   */
  @Column(name = "name")
  private String name;

  /**
   * Информация пособия
   */
  @Column(name = "info")
  private String info;

  /**
   * Документы пособия
   */
  @Column(name = "documents")
  private String documents;

  /**
   * Города пособия
   */
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_cities", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_city"))
  private List<City> cityList;

  /**
   * Критерии пособия
   */
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_criterions", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_criterion"))
  private List<City> criterionList;

  /**
   * Учреждения пособия
   */
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "benefits_institutions", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_benefit"),
    inverseJoinColumns = @JoinColumn(name = "id_institution"))
  private List<City> institutionList;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Benefit benefit = (Benefit) o;
    return id != null && Objects.equals(id, benefit.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
