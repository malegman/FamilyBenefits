package com.example.familybenefits.part_res_rest_api.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект запроса для сохранения пользователя, создания или обновления
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSave {

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
   * Список дат рождений детей пользователя
   */
  @JsonProperty("birthDateChildren")
  private List<String> birthDateChildren;

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
  @JsonProperty("idCriterionList")
  private List<String> idCriterionList;
}
