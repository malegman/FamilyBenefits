package com.example.familybenefits.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Информация о пособии
 */
@Data
@Builder
public class BenefitInfo {

  /**
   * ID пособия
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Название пособия
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация пособия
   */
  @NonNull
  @JsonProperty("info")
  private String info;

  /**
   * Документы для получения пособия
   */
  @Nullable
  @JsonProperty("documents")
  private String documents;
}
