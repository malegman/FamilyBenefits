package com.example.familybenefits.api_model.criterion_type;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Объект запроса для сохранения типа критерия в системе
 */
@Data
public class SaveCriterionTypeRequest {

  /**
   * Данные сессии пользователя для доступа к ресурса
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Название типа критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация типа критерия
   */
  @JsonProperty("info")
  private String info;
}
