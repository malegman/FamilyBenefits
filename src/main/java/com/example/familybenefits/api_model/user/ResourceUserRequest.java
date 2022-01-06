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
  private BigInteger idUser;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private BigInteger resourceToken;
}
