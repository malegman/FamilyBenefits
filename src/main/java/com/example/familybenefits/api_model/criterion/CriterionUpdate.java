package com.example.familybenefits.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Объект запроса для обновления критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionUpdate {

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
   * ID типа критерия критерия
   */
  @JsonProperty("idCriterionType")
  private String idCriterionType;
}
