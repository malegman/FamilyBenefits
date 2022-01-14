package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления администратора
 */
@Data
@Builder
public class AdminInitData {

  /**
   * Множество кратких информаций о городах
   */
  @JsonProperty("short_city_set")
  private Set<ShortObjectInfo> shortCitySet;
}