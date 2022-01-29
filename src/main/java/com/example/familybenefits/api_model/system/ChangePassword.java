package com.example.familybenefits.api_model.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {

  /**
   * Старый пароль пользователя
   */
  @JsonProperty("old_password")
  private String oldPassword;

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
