package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortInfo;
import lombok.Data;

/**
 * Критерий для подбора пособий
 */
@Data
public class CriterionInfo {

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
  private ShortInfo type;
}
