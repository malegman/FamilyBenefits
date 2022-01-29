package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления критерия.
 * Содержат в себе множество кратких информаций о типах критерий
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionInitData {

  /**
   * Множество кратких информаций о типах критерия
   */
  @JsonProperty("shortCriterionTypeSet")
  private Set<ObjectShortInfo> shortCriterionTypeSet;
}
