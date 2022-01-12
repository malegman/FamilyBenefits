package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Модель таблицы "child"
 */
@Entity
@Table(name = "child", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Child {

  /**
   * ID ребенка
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Дата рождения ребенка
   */
  @Column(name = "date_birth")
  private Date dateBirth;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Child child = (Child) o;
    return id != null && Objects.equals(id, child.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
