package com.example.familybenefits.api_model.institution;

import lombok.Data;

import java.util.UUID;

/**
 * Краткая информация об учреждении
 */
@Data
public class InstitutionShortInfo {

  /**
   * ID учреждения
   */
  private UUID id;

  /**
   * Название учреждения
   */
  private String name;
}
