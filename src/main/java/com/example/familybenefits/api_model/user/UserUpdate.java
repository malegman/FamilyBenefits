package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Объект запроса для обновления пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {

  /**
   * ID пользователя
   */
  @JsonProperty("id")
  private String id;

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
