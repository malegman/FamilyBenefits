package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Критерий для подбора пособий
 */
@Data
public class CriterionInfo {

  /**
   * Название критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация критерия
   */
  @JsonProperty("info")
  private String info;

  /**
   * Краткая информация о типе критерия
   */
  @JsonProperty("short_criterion_type")
  private ShortObjectInfo shortCriterionType;
}
