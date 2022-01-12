package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "role"
 */
@Entity
@Table(name = "role", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Role {

  /**
   * ID роли
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название роли
   */
  @Column(name = "name")
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Role role = (Role) o;
    return id != null && Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
