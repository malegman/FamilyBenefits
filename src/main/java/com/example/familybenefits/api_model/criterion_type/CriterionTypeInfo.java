package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Информация о типе критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionTypeInfo {

  /**
   * ID типа критерия
   */
  @JsonProperty("id")
  private String id;

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
