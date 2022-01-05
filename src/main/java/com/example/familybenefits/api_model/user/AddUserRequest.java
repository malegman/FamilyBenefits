package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortInfo;
import lombok.Data;

import java.util.List;

/**
 * Объект запроса для добавления пользователя в систему
 */
@Data
public class AddUserRequest {

  /**
   * Имя пользователя
   */
  private String name;

  /**
   * Эл. почта пользователя
   */
  private String email;

  /**
   * Пароль пользователя
   */
  private String password;

  /**
   * Повторно введенный пароль пользователя
   */
  private String repeatPassword;

  /**
   * Краткая информация о городе пользователя
   */
  private ShortInfo city;

  /**
   * Список критериев пользователя
   */
  private List<ShortInfo> criterionList;

  /**
   * Список дат рождений детей пользователя
   */
  private List<String> birthDateChildren;
}
