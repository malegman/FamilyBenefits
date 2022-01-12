package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса для добавления администратора
 */
@Data
@Builder
public class AdminAdd {

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
   * Краткая информация о городе администратора
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
