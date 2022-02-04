package com.example.familybenefits.dao.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Set;

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
   * Тип критерия критерия
   */
  @NonNull
  @ManyToOne
  @JoinColumn(name = "id_type", referencedColumnName = "id")
  private CriterionTypeEntity criterionTypeEntity;

  /**
   * Множество пособий критерия
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany(mappedBy = "criterionEntitySet")
  private Set<BenefitEntity> benefitEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID критерия
   */
  public CriterionEntity(@NonNull String id) {
    this.id = id;
  }
}
