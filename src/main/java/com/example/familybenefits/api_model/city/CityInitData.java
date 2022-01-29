package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления города.
 * Содержат в себе множества кратких информаций о пособиях
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityInitData {

  /**
   * Множество кратких информаций о пособиях
   */
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortBenefitSet;
}
