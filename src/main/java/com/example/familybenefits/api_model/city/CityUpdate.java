package com.example.familybenefits.api_model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для обновления города
 */
@Data
@Builder
public class CityUpdate {

  /**
   * ID города
   */
  @JsonProperty("id")
  private BigInteger id;

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
