package com.example.familybenefits.api_model.user;

import lombok.Data;

import java.math.BigInteger;

/**
 * Объект ответа пользователю на вход в систему
 */
@Data
public class UserLoginResponse {

  /**
   * ID пользователя
   */
  private BigInteger idUser;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private BigInteger resourceToken;

  /**
   * Имя пользователя
   */
  private String userName;
}
