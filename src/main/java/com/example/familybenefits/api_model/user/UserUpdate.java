package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
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
   * ID города пользователя
   */
  @Nullable
  @JsonProperty("id_city")
  private BigInteger idCity;

  /**
   * Множество ID критерий пользователя
   */
  @NonNull
  @JsonProperty("id_criterion_set")
  private Set<BigInteger> idCriterionSet;

  /**
   * Множество дат рождений детей пользователя
   */
  @NonNull
  @JsonProperty("birth_date_children")
  private Set<String> birthDateChildren;
}
