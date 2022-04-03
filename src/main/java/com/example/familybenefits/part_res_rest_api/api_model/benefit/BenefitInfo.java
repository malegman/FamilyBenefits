package com.example.familybenefits.part_res_rest_api.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private String id;

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
