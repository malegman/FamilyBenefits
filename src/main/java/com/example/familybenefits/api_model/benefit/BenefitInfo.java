package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Информация о пособии
 */
@Data
public class BenefitInfo {

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
   * Список кратких информаций о городах пособия
   */
  @JsonProperty("short_city_list")
  private List<ShortObjectInfo> shortCityList;

  /**
   * Список кратких информаций об учреждениях пособия
   */
  @JsonProperty("short_institution_list")
  private List<ShortObjectInfo> shortInstitutionList;
}
