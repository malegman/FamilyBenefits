package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для сохранения администратора в системе
 */
@Data
public class SaveAdminRequest {

  /**
   * Данные сессии пользователя для доступа к ресурсам
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Имя администратора
   */
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта администратора
   */
  @JsonProperty("email")
  private String email;

  /**
   * Пароль администратора
   */
  @JsonProperty("password")
  private String password;

  /**
   * Повторно введенный пароль администратора
   */
  @JsonProperty("repeat_password")
  private String repeatPassword;

  /**
   * ID города
   */
  @JsonProperty("id_city")
  private BigInteger idCity;
}
