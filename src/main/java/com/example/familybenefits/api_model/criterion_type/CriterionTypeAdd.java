package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * Объект запроса для добавления типа критерия
 */
@Data
@Builder
public class CriterionTypeAdd {

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
