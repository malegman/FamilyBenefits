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
 * Модель записи таблицы "cityEntity"
 */
@Entity
@Table(name = "city", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CityEntity extends ObjectEntity {

  /**
   * ID города
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Название города
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация города
   */
  @Column(name = "info")
  private String info;

  /**
   * Конструктор для создания модели по ID
   * @param id ID города
   */
  public CityEntity(@NonNull String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CityEntity that = (CityEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
