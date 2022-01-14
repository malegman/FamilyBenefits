package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для обновления типа критерия
 */
@Data
@Builder
public class CriterionTypeUpdate {

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
}