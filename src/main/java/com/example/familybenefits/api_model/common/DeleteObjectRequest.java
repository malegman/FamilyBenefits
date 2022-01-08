package com.example.familybenefits.api_model.common;

import com.example.familybenefits.api_model.user.SessionAccess;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса на удаление объекта
 */
@Data
public class DeleteObjectRequest {

  /**
   * Данные сессии пользователя для доступа к ресурса
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * ID удаляемого объекта
   */
  @JsonProperty("id_object")
  private BigInteger idObject;
}
