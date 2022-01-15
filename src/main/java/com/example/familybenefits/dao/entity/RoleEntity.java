package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
public class RoleEntity {

  /**
   * ID роли
   */
  @Nullable
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название роли
   */
  @NonNull
  @Column(name = "name")
  private String name;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    RoleEntity roleEntity = (RoleEntity) o;
    return id != null && Objects.equals(id, roleEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
