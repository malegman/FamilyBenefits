package com.example.familybenefits.api_model.criterion;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Объект запроса для обновления критерия
 */
@Data
@Builder
public class CriterionUpdate {

  /**
   * ID критерия
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название критерия
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация критерия
   */
  @NonNull
  @JsonProperty("info")
  private String info;

  /**
   * Краткая информация о типе критерия критерия
   */
  @Nullable
  @JsonProperty("short_criterion_type")
  private ShortObjectInfo shortCriterionType;
}
