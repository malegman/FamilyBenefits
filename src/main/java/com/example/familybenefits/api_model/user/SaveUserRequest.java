package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * Объект запроса для сохранения пользователя в системе
 */
@Data
public class SaveUserRequest {

  /**
   * Данные сессии пользователя для доступа к ресурсам
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Имя пользователя
   */
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта пользователя
   */
  @JsonProperty("email")
  private String email;

  /**
   * Пароль пользователя
   */
  @JsonProperty("password")
  private String password;

  /**
   * Повторно введенный пароль пользователя
   */
  @JsonProperty("repeat_password")
  private String repeatPassword;

  /**
   * ID города
   */
  @JsonProperty("id_city")
  private BigInteger idCity;

  /**
   * Список ID критериев пользователя
   */
  @JsonProperty("id_criterion_list")
  private List<BigInteger> criterionList;

  /**
   * Список дат рождений детей пользователя
   */
  @JsonProperty("birth_date_children")
  private List<String> birthDateChildren;
}
