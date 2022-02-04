package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Объект запроса для добавления пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdd {

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
  @JsonProperty("repeatPassword")
  private String repeatPassword;

  /**
   * Множество дат рождений детей пользователя
   */
  @JsonProperty("birthDateChildren")
  private Set<String> birthDateChildren;

  /**
   * Дата рождения пользователя
   */
  @JsonProperty("dateBirth")
  private String dateBirth;

  /**
   * ID города пользователя
   */
  @JsonProperty("idCity")
  private String idCity;

  /**
   * Множество ID критерий пользователя
   */
  @JsonProperty("idCriterionSet")
  private Set<String> idCriterionSet;
}
