package com.example.familybenefits.api_model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Объект запроса для обновления города
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityUpdate {

  /**
   * ID города
   */
  @JsonProperty("id")
  private String id;

  /**
   * Название города
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация города
   */
  @JsonProperty("info")
  private String info;

  /**
   * Множество ID пособий города
   */
  @JsonProperty("idBenefitSet")
  private Set<String> idBenefitSet;
}
