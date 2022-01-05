package com.example.familybenefits.api_model.common;

import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация об объекте, состоящая из его ID и названия
 */
@Data
public class ShortInfo {

  /**
   * ID объекта
   */
  private BigInteger id;

  /**
   * Название объекта
   */
  private String name;
}
