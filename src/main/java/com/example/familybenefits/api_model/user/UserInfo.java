package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация о пользователе
 */
@Data
@Builder
public class UserInfo {

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
   * Статус почты, true, если подтверждена
   */
  @JsonProperty("is_verified_email")
  private boolean isVerifiedEmail;

  /**
   * Название города пользователя
   */
  @JsonProperty("name_city")
  private String nameCity;

  /**
   * Множество дат рождений детей пользователя
   */
  @JsonProperty("birth_date_children")
  private Set<String> birthDateChildren;

  /**
   * Дата регистрации пользователя
   */
  @JsonProperty("date_registration")
  private String dateRegistration;
}
