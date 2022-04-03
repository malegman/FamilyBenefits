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
 * Модель записи таблицы "criterion_type"
 */
@Entity
@Table(name = "criterion_type", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CriterionTypeEntity extends ObjectEntity {

  /**
   * ID типа критерия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

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

  /**
   * Конструктор для создания модели по ID
   * @param id ID типа критерия
   */
  public CriterionTypeEntity(@NonNull String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionTypeEntity that = (CriterionTypeEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

