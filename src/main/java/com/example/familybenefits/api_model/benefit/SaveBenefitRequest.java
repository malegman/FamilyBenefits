package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * Объект запроса для сохранения пособия в системе
 */
@Data
public class SaveBenefitRequest {

  /**
   * Данные сессии пользователя для доступа к ресурса
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Название пособия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация пособия
   */
  @JsonProperty("info")
  private String info;

  /**
   * Документы для получения пособия
   */
  @JsonProperty("documents")
  private String documents;

  /**
   * Список ID городов пособия
   */
  @JsonProperty("id_city_list")
  private List<BigInteger> idCityList;

  /**
   * Список ID учреждений пособия
   */
  @JsonProperty("id_institution_list")
  private List<BigInteger> idInstitutionList;
}
