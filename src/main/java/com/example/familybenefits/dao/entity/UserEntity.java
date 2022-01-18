package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Модель таблицы "user"
 */
@Entity
@Table(name = "user", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class UserEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Эл. почта пользователя
   */
  @NonNull
  @Column(name = "email")
  private String email;

  /**
   * Пароль пользователя
   */
  @NonNull
  @Column(name = "password")
  private String password;

  /**
   * Дата регистрации пользователя
   */
  @NonNull
  @Column(name = "date_registration")
  private Date dateRegistration;

  /**
   * Город пользователя
   */
  @Nullable
  @ManyToOne
  @JoinColumn(name = "id_city")
  private CityEntity cityEntity;

  /**
   * Множество ролей пользователя
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "users_roles", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<RoleEntity> roleEntitySet;

  /**
   * Множество детей пользователя
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
      name = "users_children", schema = "familybenefit",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_child"))
  private Set<ChildEntity> childEntitySet;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserEntity userEntity = (UserEntity) o;
    return Objects.equals(id, userEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
