package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект запроса для обновления пользователя
 */
@Data
@Builder
public class UserUpdate {

  /**
   * ID пользователя
   */
  @JsonProperty("id")
  private BigInteger id;

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
   * Краткая информация о городе пользователя
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;

  /**
   * Множество кратких информаций о критериях пользователя
   */
  @JsonProperty("short_criterion_set")
  private Set<ShortObjectInfo> shortCriterionSet;

  /**
   * Множество дат рождений детей пользователя
   */
  @JsonProperty("birth_date_children")
  private Set<String> birthDateChildren;
}
