package com.example.familybenefits.part_res_rest_api.api_model.user;

import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Дополнительные данные для создания или обновления пользователя.
 * Содержат в себе списки кратких информаций о городах и полных критериях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitData {

  /**
   * Список кратких информаций о городах
   */
  @JsonProperty("shortCityList")
  private List<ObjectShortInfo> shortCityList;

  /**
   * Список информаций о критериях
   */
  @JsonProperty("criterionList")
  private List<CriterionInfo> criterionList;
}
