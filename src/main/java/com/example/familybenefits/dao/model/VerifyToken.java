package com.example.familybenefits.dao.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Модель таблицы "verify_token"
 */
@Entity
@Table(name = "verify_token", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VerifyToken {

  /**
   * ID пользователя
   */
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Токен подтверждения
   */
  @Column(name = "token")
  private BigInteger token;

  /**
   * Время истечения срока токена
   */
  @Column(name = "date_expiration")
  private Date dateExpiration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    VerifyToken that = (VerifyToken) o;
    return idUser != null && Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
