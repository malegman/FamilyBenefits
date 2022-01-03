package com.example.familybenefits.api_model.benefit;

import lombok.Data;

import java.util.UUID;

/**
 * Краткая информация о пособии
 */
@Data
public class BenefitShortInfo {

  /**
   * ID пособия
   */
  private UUID id;

  /**
   * Название пособия
   */
  private String name;
}
