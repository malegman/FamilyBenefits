package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация о городе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityInfo {

  /**
   * ID города
   */
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название города
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация города
   */
  @JsonProperty("info")
  private String info;

  /**
   * Множество кратких информаций о пособиях города
   */
  @JsonProperty("shortBenefitSet")
  private Set<ObjectShortInfo> shortBenefitSet;

  /**
   * Множество кратких информаций об учреждениях города
   */
  @JsonProperty("shortInstitutionSet")
  private Set<ObjectShortInfo> shortInstitutionSet;
}
