package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

/**
 * Объект запроса для обновления типа критерия
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CriterionTypeUpdate {

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
