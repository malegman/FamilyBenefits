package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
public class UserChangePwdRequest {

  /**
   * ID пользователя
   */
  private BigInteger idUser;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  @JsonProperty("resource_token")
  private BigInteger resourceToken;

  /**
   * Старый пароль пользователя
   */
  @JsonProperty("old_password")
  private String oldPassword;

  /**
   * Новый пароль пользователя
   */
  @JsonProperty("new_password")
  private String newPassword;

  /**
   * Повторно введенный новый пароль пользователя
   */
  @JsonProperty("repeat_password")
  private String repeatPassword;
}
