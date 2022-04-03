package com.example.familybenefits.part_res_rest_api.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Объект запроса для сохранения критерия, создания или обновления
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionSave {

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
   * ID типа критерия данного критерия
   */
  @JsonProperty("idCriterionType")
  private String idCriterionType;
}
