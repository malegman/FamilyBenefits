package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса на обновление объекта
 */
@Data
public class UpdateObjectRequest<T> {

  /**
   * ID обновляемого объекта
   */
  @JsonProperty("id_object")
  private BigInteger idObject;

  /**
   * Объект запроса на сохранение обновляемого объекта
   */
  @JsonProperty("save_object_request")
  private T saveObjectRequest;
}
