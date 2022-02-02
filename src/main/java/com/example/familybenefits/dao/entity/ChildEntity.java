package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Модель записи таблицы "child"
 */
@Entity
@Table(name = "child", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ChildEntity {

  /**
   * ID ребенка
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Дата рождения ребенка
   */
  @NonNull
  @Column(name = "date_birth")
  private LocalDate dateBirth;

  /**
   * Конструктор для создания модели по дате рождения
   * @param dateBirth дата рождения ребенка
   */
  public ChildEntity(@NonNull LocalDate dateBirth) {
    this.dateBirth = dateBirth;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ChildEntity childEntity = (ChildEntity) o;
    return Objects.equals(id, childEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
