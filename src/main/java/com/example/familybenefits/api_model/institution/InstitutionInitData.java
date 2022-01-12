package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления учреждения
 */
@Data
@Builder
public class InstitutionInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("short_city_set")
  private Set<ShortObjectInfo> shortCitySet;
}
