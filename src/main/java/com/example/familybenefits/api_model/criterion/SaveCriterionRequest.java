package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для сохранения криетрия в системе
 */
@Data
public class SaveCriterionRequest {

  /**
   * Данные сессии пользователя для доступа к ресурса
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Название критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация критерия
   */
  @JsonProperty("info")
  private String info;

  /**
   * ID типа критерия
   */
  @JsonProperty("id_criterion_type")
  private BigInteger idCriterionType;
}
