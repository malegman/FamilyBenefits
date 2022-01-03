package com.example.familybenefits.api_model.city;

import lombok.Data;

/**
 * Объект запроса на добавление города в систему
 */
@Data
public class AddCityRequest {

  /**
   * Название города
   */
  private String name;
}
