package com.example.familybenefits.api_model.criterion_type;

import lombok.Data;

/**
 * Объект запроса на добавление типа критерия в систему
 */
@Data
public class AddCriterionTypeRequest {

  /**
   * Название типа критерия
   */
  private String name;

  /**
   * Информация типа критерия
   */
  private String info;
}
