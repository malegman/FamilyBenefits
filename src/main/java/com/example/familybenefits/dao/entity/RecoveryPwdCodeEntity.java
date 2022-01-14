package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Модель таблицы "recovery_pwd_code"
 */
@Entity
@Table(name = "recovery_pwd_code", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecoveryPwdCodeEntity {

  /**
   * ID пользователя
   */
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Код восстановления пароля
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
    RecoveryPwdCodeEntity that = (RecoveryPwdCodeEntity) o;
    return idUser != null && Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}