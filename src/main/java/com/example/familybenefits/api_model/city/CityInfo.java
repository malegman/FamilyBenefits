package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.common.ShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * Информация о городе
 */
@Data
public class CityInfo {

  /**
   * ID города
   */
  private BigInteger id;

  /**
   * Название города
   */
  private String name;

  /**
   * Список пособий города
   */
  @JsonProperty("benefit_list")
  private List<ShortInfo> benefitList;

  /**
   * Список учреждений города
   */
  @JsonProperty("institution_list")
  private List<ShortInfo> institutionList;
}
