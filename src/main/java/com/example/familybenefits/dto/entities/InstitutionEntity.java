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
  @NonNull
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
  @NonNull
  @Column(name = "phone")
  private String phone;

  /**
   * Электронная почта учреждения
   */
  @Column(name = "email")
  private String email;

  /**
   * График работы учреждения
   */
  @NonNull
  @Column(name = "schedule")
  private String schedule;

  /**
   * ID города учреждения
   */
  @Column(name = "id_city")
  private String idCity;

  /**
   * Конструктор для создания модели по ID
   * @param id ID учреждения
   */
  public InstitutionEntity(@NonNull String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    InstitutionEntity that = (InstitutionEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
