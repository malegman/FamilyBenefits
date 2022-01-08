package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Информация о пользователе
 */
@Data
public class UserInfo {

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
   * Краткая информация о городе
   */
  @JsonProperty("short_criterion_list")
  private ShortObjectInfo shortCriterionList;

  /**
   * Список дат рождений детей пользователя
   */
  @JsonProperty("birth_date_children")
  private List<String> birthDateChildren;

  /**
   * Дата регистрации пользователя
   */
  @JsonProperty("date_registration")
  private String dateRegistration;
}
