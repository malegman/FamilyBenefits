package com.example.familybenefits.dao.entity;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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
public class RefreshTokenEntity extends ObjectEntity {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id_user")
  private String idUser;

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
}
