package com.example.familybenefits.dao.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

/**
 * Модель записи таблицы "verify_email_code"
 */
@Entity
@Table(name = "verify_email_code", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class VerifyEmailCodeEntity extends ObjectEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id_user")
  private String idUser;

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
}
