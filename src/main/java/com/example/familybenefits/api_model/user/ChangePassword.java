package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
public class ChangePassword {

  /**
   * Данные сессии пользователя для доступа к ресурсам
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

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
