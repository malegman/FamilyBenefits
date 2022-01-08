package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * Объект запроса для сохранения учреждения в системе
 */
@Data
public class SaveInstitutionRequest {

  /**
   * Данные сессии пользователя для доступа к ресурса
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Название учреждения
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация учреждения
   */
  @JsonProperty("info")
  private String info;

  /**
   * Адрес учреждения
   */
  @JsonProperty("address")
  private String address;

  /**
   * Телефон учреждения
   */
  @JsonProperty("phone")
  private String phone;

  /**
   * Электронная почта учржедения
   */
  @JsonProperty("email")
  private String email;

  /**
   * График работы учреждения
   */
  @JsonProperty("schedule")
  private String schedule;

  /**
   * ID города учреждения
   */
  @JsonProperty("id_city")
  private BigInteger idCity;

  /**
   * Список ID пособий учреждения
   */
  @JsonProperty("id_benefit_list")
  private List<BigInteger> idBenefitList;
}
