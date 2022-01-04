package com.example.familybenefits.api_model.city;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация о городе
 */
@Data
public class CityShortInfo {

  /**
   * ID города
   */
  private BigInteger id;

  /**
   * Название города
   */
  private String name;
}
