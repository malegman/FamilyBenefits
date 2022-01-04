package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.city.CityShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionShortInfo;
import lombok.Data;

import java.util.List;

/**
 * Объект запроса на добавление пособия в систему
 */
@Data
public class AddBenefitRequest {

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
   * Список городов пособия
   */
  private List<CityShortInfo> cityList;

  /**
   * Список критериев пособия
   */
  private List<CriterionShortInfo> criterionList;

  /**
   * Список учреждений пособия
   */
  private List<InstitutionShortInfo> institutionList;
}
