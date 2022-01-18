package com.example.familybenefits.api_model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Объект запроса для обновления города
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CityUpdate {

  /**
   * ID города
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название города
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация города
   */
  @Nullable
  @JsonProperty("info")
  private String info;
}
