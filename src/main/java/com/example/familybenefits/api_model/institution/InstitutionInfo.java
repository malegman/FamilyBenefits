package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ShortInfo;
import lombok.Data;

import java.math.BigInteger;

/**
 * Информация об учреждении
 */
@Data
public class InstitutionInfo {

  /**
   * Название учреждения
   */
  private String name;

  /**
   * Краткая информация о городе
   */
  private ShortInfo city;

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
