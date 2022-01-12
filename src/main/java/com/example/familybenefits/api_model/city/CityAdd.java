package com.example.familybenefits.api_model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Объект запроса для добавления города
 */
@Data
@Builder
public class CityAdd {

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
}
