package com.example.familybenefits.api_model.user;

import lombok.Data;

/**
 * Объект запроса для восстановления пароля пользователя
 */
@Data
public class UserRecoverPwdRequest {

  /**
   * Эл. почта пользователя
   */
  private String email;
}
