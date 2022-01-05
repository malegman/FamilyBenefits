package com.example.familybenefits.api_model.city;

import com.example.familybenefits.api_model.common.ShortInfo;
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
  private List<ShortInfo> benefitList;

  /**
   * Список учреждений города
   */
  private List<ShortInfo> institutionList;
}
