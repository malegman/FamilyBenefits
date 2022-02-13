package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Дополнительные данные для создания или обновления учреждения.
 * Содержат в себе множество кратких информаций о городах и пособиях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество кратких информаций о пособиях
   */
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortBenefitSet;
}
