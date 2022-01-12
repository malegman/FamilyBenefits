package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для обновления учреждения
 */
@Data
@Builder
public class InstitutionUpdate {

  /**
   * ID учреждения
   */
  @JsonProperty("id")
  private BigInteger id;

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
   * Краткая информация о городе учреждения
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
