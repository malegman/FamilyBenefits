package com.example.familybenefits.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Информация о пособии
 */
@Data
@Builder
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
}
