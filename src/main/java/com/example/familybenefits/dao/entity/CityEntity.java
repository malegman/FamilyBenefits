package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "cityEntity"
 */
@Entity
@Table(name = "city", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CityEntity {

  /**
   * ID города
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название города
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация города
   */
  @Nullable
  @Column(name = "info")
  private String info;

  /**
   * Конструктор для создания модели по ID
   * @param id ID города
   */
  public CityEntity(@NonNull BigInteger id) {
    this.id = id;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CityEntity cityEntity = (CityEntity) o;
    return Objects.equals(id, cityEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
