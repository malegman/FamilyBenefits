package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пользователя.
 * Содержат в себе множества кратких информаций о городах и полных критериях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество информаций о критериях
   */
  @JsonProperty("criterionSet")
  private Set<CriterionInfo> criterionSet;
}
