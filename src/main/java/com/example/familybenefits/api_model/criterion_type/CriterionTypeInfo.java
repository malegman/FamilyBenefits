package com.example.familybenefits.api_model.criterion_type;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация о типе критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionTypeInfo {

  /**
   * ID типа критерия
   */
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название типа критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация типа критерия
   */
  @JsonProperty("info")
  private String info;

  /**
   * Множество кратких информаций о критериях типа критерия
   */
  @JsonProperty("shortCriterionSet")
  private Set<ObjectShortInfo> shortCriterionSet;
}
