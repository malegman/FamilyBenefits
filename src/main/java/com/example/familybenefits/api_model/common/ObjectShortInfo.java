package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;

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
