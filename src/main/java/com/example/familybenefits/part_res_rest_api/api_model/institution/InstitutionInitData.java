package com.example.familybenefits.part_res_rest_api.api_model.institution;

import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Дополнительные данные для создания или обновления учреждения.
 * Содержат в себе списки кратких информаций о городах и пособиях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionInitData {

  /**
   * Список кратких информаций о городах
   */
  @JsonProperty("shortCityList")
  private List<ObjectShortInfo> shortCityList;

  /**
   * Список кратких информаций о пособиях
   */
  @JsonProperty("shortBenefitList")
  private List<ObjectShortInfo> shortBenefitList;
}
