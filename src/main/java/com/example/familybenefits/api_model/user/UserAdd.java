package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект запроса для добавления пользователя
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserAdd {

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
   * Пароль пользователя
   */
  @NonNull
  @JsonProperty("password")
  private String password;

  /**
   * Повторно введенный пароль пользователя
   */
  @NonNull
  @JsonProperty("repeat_password")
  private String repeatPassword;

  /**
   * ID города пользователя
   */
  @Nullable
  @JsonProperty("id_city")
  private BigInteger idCity;

  /**
   * Множество ID критериев пользователя
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
