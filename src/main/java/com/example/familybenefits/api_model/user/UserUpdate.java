package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта пользователя
   */
  @NonNull
  @JsonProperty("email")
  private String email;

  /**
   * Краткая информация о городе пользователя
   */
  @Nullable
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;

  /**
   * Множество кратких информаций о критериях пользователя
   */
  @NonNull
  @JsonProperty("short_criterion_set")
  private Set<ShortObjectInfo> shortCriterionSet;

  /**
   * Множество дат рождений детей пользователя
   */
  @NonNull
  @JsonProperty("birth_date_children")
  private Set<String> birthDateChildren;
}
