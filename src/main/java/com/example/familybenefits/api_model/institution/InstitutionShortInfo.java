package com.example.familybenefits.api_model.institution;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация об учреждении
 */
@Data
public class InstitutionShortInfo {

  /**
   * ID учреждения
   */
  private BigInteger id;

  /**
   * Название учреждения
   */
  private String name;
}
