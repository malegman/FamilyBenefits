package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса на восстановление пароля
 */
@Data
@Builder
public class RecoveryPassword {

  /**
   * Электронная почта пользователя
   */
  @JsonProperty("email")
  private String email;

  /**
   * Код пользователя, необходимый для восстановления пароля
   */
  @JsonProperty("recovery_pwd_code")
  private BigInteger recoveryPwdCode;

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