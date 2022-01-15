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
public class CriterionTypeEntity {

  /**
   * ID типа критерия
   */
  @Nullable
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название типа критерия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация типа критерия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionTypeEntity that = (CriterionTypeEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

