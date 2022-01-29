package com.example.familybenefits.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект запроса для обновления пособия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitUpdate {

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
   * Множество ID городов пособия
   */
  @JsonProperty("idCitySet")
  private Set<BigInteger> idCitySet;

  /**
   * Множество ID учреждений пособия
   */
  @JsonProperty("idInstitutionSet")
  private Set<BigInteger> idInstitutionSet;

  /**
   * Множество ID критерий пособия
   */
  @JsonProperty("idCriterionSet")
  private Set<BigInteger> idCriterionSet;
}
