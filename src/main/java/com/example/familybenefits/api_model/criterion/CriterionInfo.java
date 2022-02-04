package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Критерий для подбора пособий
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionInfo {

  /**
   * ID критерия
   */
  @JsonProperty("id")
  private String id;

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
  @JsonProperty("shortCriterionType")
  private ObjectShortInfo shortCriterionType;

  /**
   * Множество кратких информаций о пособиях критерия
   */
  @JsonProperty("shortBenefitSet")
  private Set<ObjectShortInfo> shortBenefitSet;
}
