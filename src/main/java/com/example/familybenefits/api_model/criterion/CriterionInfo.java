package com.example.familybenefits.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Критерий для подбора пособий
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CriterionInfo {

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
   * Название типа критерия критерия
   */
  @Nullable
  @JsonProperty("name_criterion_type")
  private String nameCriterionType;
}
