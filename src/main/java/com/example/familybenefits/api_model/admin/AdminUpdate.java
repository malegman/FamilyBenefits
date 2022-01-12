package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса для обновления администратора
 */
@Data
@Builder
public class AdminUpdate {

  /**
   * ID администратора
   */
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя администратора
   */
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта администратора
   */
  @JsonProperty("email")
  private String email;

  /**
   * Краткая информация о городе администратора
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
