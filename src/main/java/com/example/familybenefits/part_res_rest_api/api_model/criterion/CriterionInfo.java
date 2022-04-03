package com.example.familybenefits.part_res_rest_api.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Информация о критерии для подбора пособий
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
   * Название типа критерия данного критерия
   */
  @JsonProperty("nameCriterionType")
  private String nameCriterionType;
}
