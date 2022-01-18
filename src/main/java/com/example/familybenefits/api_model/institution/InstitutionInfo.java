package com.example.familybenefits.api_model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Информация об учреждении
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionInfo {

  /**
   * ID учреждения
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название учреждения
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация учреждения
   */
  @Nullable
  @JsonProperty("info")
  private String info;

  /**
   * Адрес учреждения
   */
  @NonNull
  @JsonProperty("address")
  private String address;

  /**
   * Телефон учреждения
   */
  @Nullable
  @JsonProperty("phone")
  private String phone;

  /**
   * Электронная почта учржедения
   */
  @Nullable
  @JsonProperty("email")
  private String email;

  /**
   * График работы учреждения
   */
  @Nullable
  @JsonProperty("schedule")
  private String schedule;

  /**
   * Название города учреждения
   */
  @Nullable
  @JsonProperty("name_city")
  private String nameCity;
}
