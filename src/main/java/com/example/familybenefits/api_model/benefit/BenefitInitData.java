package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пособия
 */
@Data
@Builder
public class BenefitInitData {

  /**
   * Множество кратких информаций о городах пособия
   */
  @NonNull
  @JsonProperty("short_city_set")
  private Set<ShortObjectInfo> shortCitySet;

  /**
   * Множество кратких информаций об учреждениях пособия
   */
  @NonNull
  @JsonProperty("short_institution_set")
  private Set<ShortObjectInfo> shortInstitutionSet;

  /**
   * Множество кратких информаций о критериях пособия
   */
  @NonNull
  @JsonProperty("short_criterion_set")
  private Set<ShortObjectInfo> shortCriterionSet;
}
