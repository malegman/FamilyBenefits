package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса для добавления типа критерия
 */
@Data
@Builder
public class CriterionTypeAdd {

  /**
   * Название типа критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация типа критерия
   */
  @JsonProperty("info")
  private String info;
}
