package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * Объект запроса пользователя для входа в систему
 */
@Data
@Builder
public class LoginRequest {

  /**
   * Электронная почта пользователя
   */
  @NonNull
  @JsonProperty("email")
  private String email;

  /**
   * Пароль пользователя
   */
  @NonNull
  @JsonProperty("password")
  private String password;
}
