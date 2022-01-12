package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
@Builder
public class ChangePassword {

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
