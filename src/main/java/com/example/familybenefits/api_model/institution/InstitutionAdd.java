package com.example.familybenefits.api_model.institution;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Объект запроса для добавления учреждения
 */
@Data
@Builder
public class InstitutionAdd {

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
   * Краткая информация о городе учреждения
   */
  @NonNull
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
