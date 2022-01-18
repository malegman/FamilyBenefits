package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

/**
 * Краткая информация об объекте
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
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
