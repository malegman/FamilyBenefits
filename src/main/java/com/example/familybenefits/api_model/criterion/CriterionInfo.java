package com.example.familybenefits.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Критерий для подбора пособий
 */
@Data
@Builder
public class CriterionInfo {

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
   * Название типа критерия критерия
   */
  @JsonProperty("name_criterion_type")
  private String nameCriterionType;
}
