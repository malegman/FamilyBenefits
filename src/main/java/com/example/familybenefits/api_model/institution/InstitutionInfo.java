package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.city.CityShortInfo;
import lombok.Data;

import java.math.BigInteger;

/**
 * Информация об учреждении
 */
@Data
public class InstitutionInfo {

  /**
   * ID учреждения
   */
  private BigInteger id;

  /**
   * Название учреждения
   */
  private String name;

  /**
   * Краткая информация о городе
   */
  private CityShortInfo city;

  /**
   * Адрес учреждения
   */
  private String address;

  /**
   * Телефон учреждения
   */
  private String phone;

  /**
   * Электронная почта учржедения
   */
  private String email;

  /**
   * График работы учреждения
   */
  private String schedule;
}
