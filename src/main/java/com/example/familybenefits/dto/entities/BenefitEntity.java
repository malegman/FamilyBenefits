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
 * Модель записи таблицы "benefit"
 */
@Entity
@Table(name = "benefit", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class BenefitEntity extends ObjectEntity {

  /**
   * ID пособия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Название пособия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация пособия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  /**
   * Документы пособия
   */
  @NonNull
  @Column(name = "documents")
  private String documents;

  /**
   * Конструктор для создания модели по ID
   * @param id ID пособия
   */
  public BenefitEntity(@NonNull String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    BenefitEntity that = (BenefitEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
