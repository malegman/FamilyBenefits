package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Модель таблицы "user"
 */
@Entity
@Table(name = "user", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

  /**
   * ID пользователя
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @Column(name = "name")
  private String name;

  /**
   * Эл. почта пользователя
   */
  @Column(name = "email")
  private String email;

  /**
   * Пароль пользователя
   */
  @Column(name = "password")
  private String password;

  /**
   * Дата регистрации пользователя
   */
  @Column(name = "date_registration")
  private Date dateRegistration;

  /**
   * Город пользователя
   */
  @ManyToOne
  @JoinColumn(name = "id_city")
  private City city;

  /**
   * Роль пользователя
   */
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
    name = "users_roles", schema = "familybenefit",
    joinColumns = @JoinColumn(name = "id_user"),
    inverseJoinColumns = @JoinColumn(name = "id_role"))
  private List<Role> role;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
