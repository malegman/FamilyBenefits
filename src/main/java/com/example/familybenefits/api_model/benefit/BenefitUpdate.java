package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект запроса для обновления пособия
 */
@Data
@Builder
public class BenefitUpdate {

  /**
   * ID пособия
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

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
