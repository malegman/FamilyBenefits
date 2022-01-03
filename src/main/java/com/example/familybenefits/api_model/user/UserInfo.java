package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.city.CityShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionShortInfo;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Информация о пользователе
 */
@Data
public class UserInfo {

  /**
   * ID пользователя
   */
  private UUID id;

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
  private CityShortInfo city;

  /**
   * Список критерий пользователя
   */
  private List<CriterionShortInfo> criterionList;

  /**
   * Список дат рождений детей пользователя
   */
  private List<String> birthDateChildren;

  /**
   * Дата регистрации пользователя
   */
  private String dateRegistration;
}
