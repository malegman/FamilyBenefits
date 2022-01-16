package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название критерия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация критерия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  /**
   * Тип критерия критерия
   */
  @Nullable
  @ManyToOne
  @JoinColumn(name = "id_type")
  private CriterionTypeEntity type;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionEntity criterionEntity = (CriterionEntity) o;
    return Objects.equals(id, criterionEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
