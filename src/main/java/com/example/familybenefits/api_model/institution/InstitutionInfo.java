package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация об учреждении
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionInfo {

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
  @JsonProperty("shortCity")
  private ObjectShortInfo shortCity;

  /**
   * Множество кратких информаций о пособиях учреждения
   */
  @JsonProperty("shortBenefitSet")
  private Set<ObjectShortInfo> shortBenefitSet;
}
