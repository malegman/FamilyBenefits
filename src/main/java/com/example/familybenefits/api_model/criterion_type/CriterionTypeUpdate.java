package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Объект запроса для обновления типа критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionTypeUpdate {

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
