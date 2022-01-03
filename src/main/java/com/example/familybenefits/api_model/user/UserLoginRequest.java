package com.example.familybenefits.api_model.user;

import lombok.Data;

/**
 * Объект запроса пользователя для входа в систему
 */
@Data
public class UserLoginRequest {

  /**
   * Эл. почта пользователя
   */
  private String email;

  /**
   * Пароль пользователя
   */
  private String password;
}
