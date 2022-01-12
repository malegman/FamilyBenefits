package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Объект запроса для добавления пособия
 */
@Data
@Builder
public class BenefitAdd {

  /**
   * Название пособия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация пособия
   */
  @JsonProperty("info")
  private String info;

  /**
   * Документы для получения пособия
   */
  @JsonProperty("documents")
  private String documents;

  /**
   * Множество кратких информаций о городах пособия
   */
  @JsonProperty("short_city_set")
  private Set<ShortObjectInfo> shortCitySet;

  /**
   * Множество кратких информаций об учреждениях пособия
   */
  @JsonProperty("short_institution_set")
  private Set<ShortObjectInfo> shortInstitutionSet;

  /**
   * Множество кратких информаций о критериях пособия
   */
  @JsonProperty("short_criterion_set")
  private Set<ShortObjectInfo> shortCriterionSet;
}
