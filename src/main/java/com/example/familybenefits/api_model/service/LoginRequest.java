package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса пользователя для входа в систему
 */
@Data
@Builder
public class LoginRequest {

  /**
   * Электронная почта пользователя
   */
  @JsonProperty("email")
  private String email;

  /**
   * Пароль пользователя
   */
  @JsonProperty("password")
  private String password;
}
