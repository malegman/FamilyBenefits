package com.example.familybenefits.api_model.criterion;

import lombok.Data;

import java.util.UUID;

/**
 * Краткая информация о критерии
 */
@Data
public class CriterionShortInfo {

  /**
   * ID критерия
   */
  private UUID id;

  /**
   * Название критерия
   */
  private String name;
}
