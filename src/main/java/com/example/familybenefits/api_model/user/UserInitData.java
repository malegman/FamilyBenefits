package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пользователя.
 * Содержат в себе множества кратких информаций о городах и критериях
 */
@Data
@Builder
public class UserInitData {

  /**
   * Множество кратких информаций о городах
   */
  @NonNull
  @JsonProperty("short_city_set")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество информаций о критериях
   */
  @NonNull
  @JsonProperty("criterion_set")
  private Set<CriterionInfo> criterionSet;
}
