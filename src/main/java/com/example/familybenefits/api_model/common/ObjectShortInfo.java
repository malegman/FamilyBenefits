package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

/**
 * Краткая информация об объекте
 */
@Data
@Builder
public class ObjectShortInfo {

  /**
   * ID объекта
   */
  @NonNull
  @JsonProperty("id_object")
  private BigInteger idObject;

  /**
   * Название объекта
   */
  @NonNull
  @JsonProperty("name_object")
  private String nameObject;
}
