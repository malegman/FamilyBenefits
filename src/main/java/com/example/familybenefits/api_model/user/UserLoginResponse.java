package com.example.familybenefits.api_model.user;

import lombok.Data;

import java.util.UUID;

/**
 * Объект ответа пользователю на вход в систему
 */
@Data
public class UserLoginResponse {

  /**
   * ID пользователя
   */
  private UUID userID;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private String resourceToken;

  /**
   * Имя пользователя
   */
  private String userName;
}
