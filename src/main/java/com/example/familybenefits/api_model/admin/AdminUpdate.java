package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя администратора
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта администратора
   */
  @NonNull
  @JsonProperty("email")
  private String email;

  /**
   * Краткая информация о городе администратора
   */
  @Nullable
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
