package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления критерия
 */
@Data
@Builder
public class CriterionInitData {

  /**
   * Множество кратких информаций о типах критерия
   */
  @JsonProperty("short_criterion_type_set")
  private Set<ShortObjectInfo> shortCriterionTypeSet;
}
