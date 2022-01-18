package com.example.familybenefits.api_model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Объект запроса для обновления учреждения
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class InstitutionUpdate {

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
   * ID города учреждения
   */
  @NonNull
  @JsonProperty("id_city")
  private BigInteger idCity;
}
