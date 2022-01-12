package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса для добавления критерия
 */
@Data
@Builder
public class CriterionAdd {

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
   * Краткая информация о типе критерия критерия
   */
  @JsonProperty("short_criterion_type")
  private ShortObjectInfo shortCriterionType;
}
