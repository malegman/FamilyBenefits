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
 * Модель таблицы "reset_code"
 */
@Entity
@Table(name = "reset_code", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResetCode {

  /**
   * ID пользователя
   */
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Код сброса сессии
   */
  @Column(name = "code")
  private BigInteger code;

  /**
   * Время истечения срока кода
   */
  @Column(name = "date_expiration")
  private Date dateExpiration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ResetCode resetCode = (ResetCode) o;
    return idUser != null && Objects.equals(idUser, resetCode.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
