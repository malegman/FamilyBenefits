package com.example.familybenefits.api_model.benefit;

import com.example.familybenefits.api_model.common.ShortInfo;
import lombok.Data;

import java.util.List;

/**
 * Информация о пособии
 */
@Data
public class BenefitInfo {

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
  private List<ShortInfo> cityList;

  /**
   * Список критериев пособия
   */
  private List<ShortInfo> criterionList;

  /**
   * Список учреждений пособия
   */
  private List<ShortInfo> institutionList;
}
