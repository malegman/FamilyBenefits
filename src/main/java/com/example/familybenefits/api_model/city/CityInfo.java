package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.benefit.BenefitShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionShortInfo;
import lombok.Data;

import java.util.List;
import java.math.BigInteger;

/**
 * Информация о городе
 */
@Data
public class CityInfo {

  /**
   * ID города
   */
  private BigInteger id;

  /**
   * Название города
   */
  private String name;

  /**
   * Список пособий города
   */
  private List<BenefitShortInfo> benefitList;

  /**
   * Список учреждений города
   */
  private List<InstitutionShortInfo> institutionList;
}
