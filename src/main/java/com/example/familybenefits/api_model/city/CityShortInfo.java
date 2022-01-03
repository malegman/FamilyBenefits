package com.example.familybenefits.api_model.city;

import lombok.Data;

import java.util.UUID;

/**
 * Краткая информация о городе
 */
@Data
public class CityShortInfo {

  /**
   * ID города
   */
  private UUID id;

  /**
   * Название города
   */
  private String name;
}
