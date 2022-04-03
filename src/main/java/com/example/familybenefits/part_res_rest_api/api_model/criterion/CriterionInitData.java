package com.example.familybenefits.part_res_rest_api.api_model.criterion;

import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Дополнительные данные для создания или обновления критерия.
 * Содержат в себе список кратких информаций о типах критерий
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionInitData {

  /**
   * Список кратких информаций о типах критерия
   */
  @JsonProperty("shortCriterionTypeList")
  private List<ObjectShortInfo> shortCriterionTypeList;
}
