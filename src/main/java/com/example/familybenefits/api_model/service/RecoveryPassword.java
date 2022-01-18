package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

/**
 * Объект запроса на восстановление пароля
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecoveryPassword {

  /**
   * Электронная почта пользователя
   */
  @NonNull
  @JsonProperty("email")
  private String email;

  /**
   * Код пользователя, необходимый для восстановления пароля
   */
  @NonNull
  @JsonProperty("recovery_pwd_code")
  private BigInteger recoveryPwdCode;

  /**
   * Новый пароль пользователя
   */
  @NonNull
  @JsonProperty("new_password")
  private String newPassword;

  /**
   * Повторно введенный новый пароль пользователя
   */
  @NonNull
  @JsonProperty("repeat_password")
  private String repeatPassword;
}
