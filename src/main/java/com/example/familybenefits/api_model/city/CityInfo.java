package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Информация о городе
 */
@Data
public class CityInfo {

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
   * Список кратких информаций о пособиях города
   */
  @JsonProperty("benefit_list")
  private List<ShortObjectInfo> benefitList;

  /**
   * Список кратких информаций об учреждениях города
   */
  @JsonProperty("institution_list")
  private List<ShortObjectInfo> institutionList;
}
