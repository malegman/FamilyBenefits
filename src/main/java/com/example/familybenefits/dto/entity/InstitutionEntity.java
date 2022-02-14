package com.example.familybenefits.dto.entity;

import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

/**
 * Модель записи таблицы "institution"
 */
@Entity
@Table(name = "institution", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class InstitutionEntity extends ObjectEntity {

  /**
   * ID учреждения
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Название учреждения
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация учреждения
   */
  @Nullable
  @Column(name = "info")
  private String info;

  /**
   * Адрес учреждения
   */
  @NonNull
  @Column(name = "address")
  private String address;

  /**
   * Телефон учреждения
   */
  @Nullable
  @Column(name = "phone")
  private String phone;

  /**
   * Электронная почта учреждения
   */
  @Nullable
  @Column(name = "email")
  private String email;

  /**
   * График работы учреждения
   */
  @Nullable
  @Column(name = "schedule")
  private String schedule;

  /**
   * Город учреждения
   */
  @NonNull
  @ManyToOne
  @JoinColumn(name = "id_city", referencedColumnName = "id")
  private CityEntity cityEntity;

  /**
   * Множество пособий учреждения
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany(mappedBy = "institutionEntitySet")
  private Set<BenefitEntity> benefitEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID учреждения
   */
  public InstitutionEntity(@NonNull String id) {
    this.id = id;
  }
}
