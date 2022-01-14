package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "criterion"
 */
@Entity
@Table(name = "criterion", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CriterionEntity {

  /**
   * ID критерия
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название критерия
   */
  @Column(name = "name")
  private String name;

  /**
   * Информация критерия
   */
  @Column(name = "info")
  private String info;

  /**
   * Тип критерия критерия
   */
  @ManyToOne
  @JoinColumn(name = "id_type")
  private CriterionTypeEntity type;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionEntity criterionEntity = (CriterionEntity) o;
    return id != null && Objects.equals(id, criterionEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}