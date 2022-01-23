package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Модель записи таблицы "recovery_pwd_code"
 */
@Entity
@Table(name = "recovery_pwd_code", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class RecoveryPwdCodeEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Код восстановления пароля
   */
  @NonNull
  @Column(name = "code")
  private BigInteger code;

  /**
   * Время истечения срока кода
   */
  @NonNull
  @Column(name = "date_expiration")
  private Date dateExpiration;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    RecoveryPwdCodeEntity that = (RecoveryPwdCodeEntity) o;
    return Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
