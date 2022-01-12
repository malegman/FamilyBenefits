package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пользователя
 */
@Data
@Builder
public class UserInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("short_city_set")
  private Set<ShortObjectInfo> shortCitySet;

  /**
   * Множество информаций о критериях
   */
  @JsonProperty("criterion_set")
  private Set<CriterionInfo> criterionSet;
}
