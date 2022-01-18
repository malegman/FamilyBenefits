package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления критерия.
 * Содержат в себе множество кратких информаций о типах критериев
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionInitData {

  /**
   * Множество кратких информаций о типах критерия
   */
  @NonNull
  @JsonProperty("short_criterion_type_set")
  private Set<ObjectShortInfo> shortCriterionTypeSet;
}
