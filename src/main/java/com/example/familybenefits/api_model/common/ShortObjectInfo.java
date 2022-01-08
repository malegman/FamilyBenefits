package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Краткая информация об объекте
 */
@Data
public class ShortObjectInfo {

  /**
   * ID объекта
   */
  @JsonProperty("id_object")
  private BigInteger idObject;

  /**
   * Название объекта
   */
  @JsonProperty("name_object")
  private String nameObject;
}
