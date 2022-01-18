package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления администратора.
 * Содержат в себе множество кратких информаций о городах
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminInitData {

  /**
   * Множество кратких информаций о городах
   */
  @NonNull
  @JsonProperty("short_city_set")
  private Set<ObjectShortInfo> shortCitySet;
}
