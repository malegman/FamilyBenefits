package com.example.familybenefits.api_model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Объект запроса для добавления города
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityAdd {

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
