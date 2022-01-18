package com.example.familybenefits.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
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
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация пособия
   */
  @NonNull
  @JsonProperty("info")
  private String info;

  /**
   * Документы для получения пособия
   */
  @Nullable
  @JsonProperty("documents")
  private String documents;

  /**
   * Множество ID городов пособия
   */
  @NonNull
  @JsonProperty("id_city_set")
  private Set<BigInteger> idCitySet;

  /**
   * Множество ID учреждений пособия
   */
  @NonNull
  @JsonProperty("short_institution_set")
  private Set<BigInteger> idInstitutionSet;

  /**
   * Множество ID критериев пособия
   */
  @NonNull
  @JsonProperty("short_criterion_set")
  private Set<BigInteger> idCriterionSet;
}
