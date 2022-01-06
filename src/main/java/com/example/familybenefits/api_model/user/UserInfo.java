package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.math.BigInteger;

/**
 * Информация о пользователе
 */
@Data
public class UserInfo {

  /**
   * Имя пользователя
   */
  private String name;

  /**
   * Эл. почта пользователя
   */
  private String email;

  /**
   * Краткая информация о городе
   */
  private ShortInfo city;

  /**
   * Список критерий пользователя
   */
  @JsonProperty("criterion_list")
  private List<ShortInfo> criterionList;

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
