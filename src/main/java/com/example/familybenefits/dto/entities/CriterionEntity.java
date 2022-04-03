package com.example.familybenefits.dto.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Модель записи таблицы "criterion"
 */
@Entity
@Table(name = "criterion", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CriterionEntity extends ObjectEntity {

  /**
   * ID критерия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

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
   * ID типа критерия данного критерия
   */
  @Column(name = "id_type")
  private String idCriterionType;

  /**
   * Конструктор для создания модели по ID
   * @param id ID критерия
   */
  public CriterionEntity(@NonNull String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionEntity that = (CriterionEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
