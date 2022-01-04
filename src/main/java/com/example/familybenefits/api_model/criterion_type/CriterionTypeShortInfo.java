package com.example.familybenefits.api_model.criterion_type;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация о типе критерия
 */
@Data
public class CriterionTypeShortInfo {

  /**
   * ID типа критерия
   */
  private BigInteger id;

  /**
   * Название типа критерия
   */
  private String name;
}
