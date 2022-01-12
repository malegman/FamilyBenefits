package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для обновления критерия
 */
@Data
@Builder
public class CriterionUpdate {

  /**
   * ID критерия
   */
  @JsonProperty("id")
  private BigInteger id;

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
