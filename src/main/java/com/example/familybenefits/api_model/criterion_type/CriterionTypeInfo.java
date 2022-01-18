package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

/**
 * Информация о типе критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionTypeInfo {

  /**
   * ID типа критерия
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название типа критерия
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация типа критерия
   */
  @NonNull
  @JsonProperty("info")
  private String info;
}
