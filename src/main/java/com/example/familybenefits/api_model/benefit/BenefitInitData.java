package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * Дополнительные данные для добавления или обновления пособия.
 * Содержат в себе множества кратких информаций о городах, учреждениях и критериях
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BenefitInitData {

  /**
   * Множество кратких информаций о городах пособия
   */
  @NonNull
  @JsonProperty("short_city_set")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество кратких информаций об учреждениях пособия
   */
  @NonNull
  @JsonProperty("short_institution_set")
  private Set<ObjectShortInfo> shortInstitutionSet;

  /**
   * Множество кратких информаций о критериях пособия
   */
  @NonNull
  @JsonProperty("short_criterion_set")
  private Set<ObjectShortInfo> shortCriterionSet;
}
