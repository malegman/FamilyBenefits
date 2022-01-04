package com.example.familybenefits.api_model.criterion;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация о критерии
 */
@Data
public class CriterionShortInfo {

  /**
   * ID критерия
   */
  private BigInteger id;

  /**
   * Название критерия
   */
  private String name;
}
