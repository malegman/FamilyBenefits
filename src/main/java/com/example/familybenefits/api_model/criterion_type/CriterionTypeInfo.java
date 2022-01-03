package com.example.familybenefits.api_model.criterion_type;

import lombok.Data;

import java.util.UUID;

/**
 * Информация о типе критерия
 */
@Data
public class CriterionTypeInfo {

  /**
   * ID типа критерия
   */
  private UUID id;

  /**
   * Название типа критерия
   */
  private String name;

  /**
   * Информация типа критерия
   */
  private String info;
}
