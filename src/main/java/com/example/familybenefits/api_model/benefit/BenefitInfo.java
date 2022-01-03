package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.city.CityShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionShortInfo;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Информация о пособии
 */
@Data
public class BenefitInfo {

  /**
   * ID пособия
   */
  private UUID id;

  /**
   * Название пособия
   */
  private String name;

  /**
   * Информация пособия
   */
  private String info;

  /**
   * Документы для получения пособия
   */
  private String documents;

  /**
   * Краткая информация о городе
   */
  private CityShortInfo city;

  /**
   * Список критериев пособия
   */
  private List<CriterionShortInfo> criterionList;

  /**
   * Список учреждений пособия
   */
  private List<InstitutionShortInfo> institutionList;
}
