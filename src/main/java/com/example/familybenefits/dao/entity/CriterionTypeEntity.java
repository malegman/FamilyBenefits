package com.example.familybenefits.dao.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

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
   * Множество критерий типа критерия
   */
  @NonNull
  @ToString.Exclude
  @OneToMany(mappedBy = "criterionTypeEntity")
  private Set<CriterionEntity> criterionEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID типа критерия
   */
  public CriterionTypeEntity(@NonNull String id) {
    this.id = id;
  }
}

