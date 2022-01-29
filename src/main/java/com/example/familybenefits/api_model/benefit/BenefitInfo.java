package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация о пособии
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitInfo {

  /**
   * ID пособия
   */
  @JsonProperty("id")
  private BigInteger id;

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
  @JsonProperty("shortCitySet")
  private Set<ObjectShortInfo> shortCitySet;

  /**
   * Множество кратких информаций об учреждениях пособия
   */
  @JsonProperty("shortInstitutionSet")
  private Set<ObjectShortInfo> shortInstitutionSet;

  /**
   * Множество информаций о критериях пособия
   */
  @JsonProperty("criterionSet")
  private Set<CriterionInfo> criterionSet;
}
