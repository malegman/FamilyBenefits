package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * Модель таблицы "verify_email_code"
 */
@Entity
@Table(name = "verify_email_code", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class VerifyEmailCodeEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Код подтверждения почты
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
    VerifyEmailCodeEntity that = (VerifyEmailCodeEntity) o;
    return Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
