package com.example.familybenefits.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Объект запроса для добавления пособия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
   * Множество ID городов пособия
   */
  @JsonProperty("idCitySet")
  private Set<String> idCitySet;

  /**
   * Множество ID учреждений пособия
   */
  @JsonProperty("idInstitutionSet")
  private Set<String> idInstitutionSet;

  /**
   * Множество ID критерий пособия
   */
  @JsonProperty("idCriterionSet")
  private Set<String> idCriterionSet;
}
