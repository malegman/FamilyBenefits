package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;

/**
 * Модель записи таблицы "admin"
 */
@Entity
@Table(name = "admin", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class AdminEntity implements DBPreparer {

  /**
   * ID администратора
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Имя администратора
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Эллектронная почта администратора
   */
  @NonNull
  @Column(name = "email")
  private String email;

  /**
   * Статус подтверждения почты
   */
  @Column(name = "is_verified_email")
  private boolean isVerifiedEmail;

  /**
   * Пароль администратора
   */
  @NonNull
  @Column(name = "password")
  private String password;

  /**
   * Обработывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  @Override
  public DBPreparer prepareForDB(Function<String, String> prepareFunc) {

    name = prepareFunc.apply(name);
    email = prepareFunc.apply(email);
    password = prepareFunc.apply(password);

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    AdminEntity that = (AdminEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
