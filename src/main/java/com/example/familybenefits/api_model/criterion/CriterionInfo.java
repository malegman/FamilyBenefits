package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeShortInfo;
import lombok.Data;

import java.util.UUID;

/**
 * Критерий для подбора пособий
 */
@Data
public class CriterionInfo {

  /**
   * ID критерия
   */
  private UUID id;

  /**
   * Название критерия
   */
  private String name;

  /**
   * Краткая информация о типе критерия
   */
  private CriterionTypeShortInfo type;
}
