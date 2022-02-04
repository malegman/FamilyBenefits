package com.example.familybenefits.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект запроса для добавления критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
   * ID типа критерия критерия
   */
  @JsonProperty("idCriterionType")
  private String idCriterionType;
}
