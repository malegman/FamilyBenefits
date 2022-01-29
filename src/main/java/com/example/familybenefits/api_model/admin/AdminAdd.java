package com.example.familybenefits.api_model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект запроса для добавления администратора
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
  @JsonProperty("repeatPassword")
  private String repeatPassword;
}
