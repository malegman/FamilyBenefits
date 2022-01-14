package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "cityEntity"
 */
@Entity
@Table(name = "city", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CityEntity {

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
   * Информация города
   */
  @Column(name = "info")
  private String info;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CityEntity cityEntity = (CityEntity) o;
    return id != null && Objects.equals(id, cityEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}