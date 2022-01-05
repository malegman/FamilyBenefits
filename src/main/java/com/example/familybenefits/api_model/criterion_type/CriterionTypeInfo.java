package com.example.familybenefits.api_model.criterion_type;

import lombok.Data;

import java.math.BigInteger;

/**
 * Информация о типе критерия
 */
@Data
public class CriterionTypeInfo {

  /**
   * Название типа критерия
   */
  private String name;

  /**
   * Информация типа критерия
   */
  private String info;
}
