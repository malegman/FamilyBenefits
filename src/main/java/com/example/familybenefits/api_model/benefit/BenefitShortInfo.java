package com.example.familybenefits.api_model.benefit;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация о пособии
 */
@Data
public class BenefitShortInfo {

  /**
   * ID пособия
   */
  private BigInteger id;

  /**
   * Название пособия
   */
  private String name;
}
