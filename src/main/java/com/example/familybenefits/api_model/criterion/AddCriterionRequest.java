package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeShortInfo;
import lombok.Data;

/**
 * Объект запроса на добавление критерия в систему
 */
@Data
public class AddCriterionRequest {

  /**
   * Название критерия
   */
  private String name;

  /**
   * Краткая информация о типе критерия
   */
  private CriterionTypeShortInfo type;
}
