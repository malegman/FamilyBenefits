package com.example.familybenefits.dto.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Модель записи таблицы "user"
 */
@Entity
@Table(name = "user", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class UserEntity extends ObjectEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id")
  private String id;

  /**
   * Имя пользователя
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Электронная почта пользователя
   */
  @NonNull
  @Column(name = "email")
  private String email;

  /**
   * Дата рождения пользователя
   */
  @Column(name = "date_birth")
  private LocalDate dateBirth;

  /**
   * Дата последнего выбора критерий пользователя
   */
  @Column(name = "date_select_criterion")
  private LocalDate dateSelectCriterion;

  /**
   * Флаг свежести подобранных пособий
   */
  @Column(name = "is_fresh_benefits")
  private boolean isFreshBenefits;

  /**
   * ID города пользователя
   */
  @Column(name = "id_city")
  private String idCity;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserEntity userEntity = (UserEntity) o;
    return id.equals(userEntity.id) && email.equals(userEntity.email);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
