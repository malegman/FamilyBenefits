package com.example.familybenefits.api_model.user;

import lombok.Data;

import java.util.UUID;

/**
 * Объект запроса для получения данных пользователя
 */
@Data
public class ResourceUserRequest {

  /**
   * ID пользователя
   */
  private UUID userID;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private String resourceToken;
}
