package com.example.familybenefits.api_model.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * Объект запроса на восстановление пароля
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryPassword {

  /**
   * Электронная почта пользователя
   */
  @JsonProperty("email")
  private String email;

  /**
   * Код пользователя, необходимый для восстановления пароля
   */
  @JsonProperty("recoveryPwdCode")
  private BigInteger recoveryPwdCode;

  /**
   * Новый пароль пользователя
   */
  @JsonProperty("newPassword")
  private String newPassword;

  /**
   * Повторно введенный новый пароль пользователя
   */
  @JsonProperty("repeatPassword")
  private String repeatPassword;
}
