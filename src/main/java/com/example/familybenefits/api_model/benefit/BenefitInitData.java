package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пособия.
 * Содержат в себе множества кратких информаций о городах, учреждениях и полных критериях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество кратких информаций об учреждениях
   */
  @JsonProperty("shortInstitutionSet")
  private Set<ObjectShortInfo> shortInstitutionSet;

  /**
   * Множество информаций о критериях
   */
  @JsonProperty("criterionSet")
  private Set<CriterionInfo> criterionSet;
}
