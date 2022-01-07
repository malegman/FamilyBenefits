package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * Модель таблицы "city"
 */
@Entity
@Table(name = "city", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class City {

  /**
   * ID города
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название города
   */
  @Column(name = "name")
  private String name;

  /**
   * Учреждения города
   */
  @ToString.Exclude
  @OneToMany(mappedBy = "city")
  private List<Institution> institutionList;

  /**
   * Пособия города
   */
  @ToString.Exclude
  @ManyToMany(mappedBy = "cityList")
  private List<Benefit> benefitList;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    City city = (City) o;
    return id != null && Objects.equals(id, city.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
