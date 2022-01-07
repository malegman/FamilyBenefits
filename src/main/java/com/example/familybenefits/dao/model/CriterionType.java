package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "criterion_type"
 */
@Entity
@Table(name = "criterion_type", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CriterionType {

  /**
   * ID типа критерия
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название типа критерия
   */
  @Column(name = "name")
  private String name;

  /**
   * Информация типа критерия
   */
  @Column(name = "info")
  private String info;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionType that = (CriterionType) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

