package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * Краткая информация об объекте
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectShortInfo {

  /**
   * ID объекта
   */
  @JsonProperty("idObject")
  private BigInteger idObject;

  /**
   * Название объекта
   */
  @JsonProperty("nameObject")
  private String nameObject;
}
