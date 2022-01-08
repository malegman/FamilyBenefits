package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Объект ответа пользователю на вход в систему
 */
@Data
public class LoginResponse {

  /**
   * Данные сессии пользователя для доступа к ресурсам
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Имя пользователя
   */
  @JsonProperty("user_name")
  private String userName;
}
