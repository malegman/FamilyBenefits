package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "resource_token"
 */
@Entity
@Table(name = "resource_token", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResourceToken {

  /**
   * ID пользователя
   */
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Токен ресурсов
   */
  @Column(name = "token")
  private BigInteger token;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ResourceToken that = (ResourceToken) o;
    return idUser != null && Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
