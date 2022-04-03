package com.example.familybenefits.part_res_rest_api.api_model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private String id;

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
   * Электронная почта учреждения
   */
  @JsonProperty("email")
  private String email;

  /**
   * График работы учреждения
   */
  @JsonProperty("schedule")
  private String schedule;

  /**
   * Название города учреждения
   */
  @JsonProperty("nameCity")
  private String nameCity;
}
