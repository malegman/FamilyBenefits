package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Информация об учреждении
 */
@Data
public class InstitutionInfo {

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
   * Краткая информация о городе
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;

  /**
   * Список кратких информаций о пособиях учреждения
   */
  @JsonProperty("short_benefit_list")
  private List<ShortObjectInfo> shortBenefitList;
}
