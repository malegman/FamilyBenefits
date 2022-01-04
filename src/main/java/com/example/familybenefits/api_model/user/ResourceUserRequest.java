package com.example.familybenefits.api_model.user;

import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для получения данных пользователя
 */
@Data
public class ResourceUserRequest {

  /**
   * ID пользователя
   */
  private BigInteger userID;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private String resourceToken;
}
