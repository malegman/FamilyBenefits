package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeShortInfo;
import lombok.Data;

import java.math.BigInteger;

/**
 * Критерий для подбора пособий
 */
@Data
public class CriterionInfo {

  /**
   * ID критерия
   */
  private BigInteger id;

  /**
   * Название критерия
   */
  private String name;

  /**
   * Информация критерия
   */
  private String info;

  /**
   * Краткая информация о типе критерия
   */
  private CriterionTypeShortInfo type;
}
