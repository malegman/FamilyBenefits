package com.example.familybenefits.api_model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект запроса для добавления учреждения
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionAdd {

  /**
   * Название учреждения
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация учреждения
   */
  @JsonProperty("info")
  private String info;

  /**
   * Адрес учреждения
   */
  @JsonProperty("address")
  private String address;

  /**
   * Телефон учреждения
   */
  @JsonProperty("phone")
  private String phone;

  /**
   * Электронная почта учржедения
   */
  @JsonProperty("email")
  private String email;

  /**
   * График работы учреждения
   */
  @JsonProperty("schedule")
  private String schedule;

  /**
   * ID города учреждения
   */
  @JsonProperty("idCity")
  private BigInteger idCity;

  /**
   * Множество ID пособий учреждения
   */
  @JsonProperty("idBenefitSet")
  private Set<BigInteger> idBenefitSet;
}
