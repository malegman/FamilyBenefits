package com.example.familybenefits.api_model.user;

import lombok.Data;

import java.util.UUID;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
public class UserChangePwdRequest {

  /**
   * ID пользователя
   */
  private UUID userID;

  /**
   * Токен пользователя, позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private String resourceToken;

  /**
   * Старый пароль пользователя
   */
  private String oldPassword;

  /**
   * Новый пароль пользователя
   */
  private String newPassword;

  /**
   * Повторно введенный новый пароль пользователя
   */
  private String repeatPassword;
}
