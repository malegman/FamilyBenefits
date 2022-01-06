package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortInfo;
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
  private String name;

  /**
   * Информация пособия
   */
  private String info;

  /**
   * Документы для получения пособия
   */
  private String documents;

  /**
   * Список городов пособия
   */
  @JsonProperty("city_list")
  private List<ShortInfo> cityList;

  /**
   * Список критериев пособия
   */
  @JsonProperty("criterion_list")
  private List<ShortInfo> criterionList;

  /**
   * Список учреждений пособия
   */
  @JsonProperty("institution_list")
  private List<ShortInfo> institutionList;
}
