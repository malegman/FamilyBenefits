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
 * Модель записи таблицы "refresh_token"
 */
@Entity
@Table(name = "refresh_token", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class RefreshTokenEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id_user")
  private BigInteger idUser;

  /**
   * Токен обновления токена доступа
   */
  @NonNull
  @Column(name = "token")
  private String token;

  /**
   * Время истечения срока токена
   */
  @NonNull
  @Column(name = "date_expiration")
  private Date dateExpiration;

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    RefreshTokenEntity that = (RefreshTokenEntity) o;
    return Objects.equals(idUser, that.idUser);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
