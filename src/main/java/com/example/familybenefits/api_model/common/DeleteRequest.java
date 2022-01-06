package com.example.familybenefits.api_model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Запрос от пользователя на удаление объекта
 */
@Data
public class DeleteRequest {

  /**
   * ID удаляемого объекта
   */
  @JsonProperty("id_object")
  private BigInteger idObject;

  /**
   * ID пользователя, который отправил запрос на удаление
   */
  @JsonProperty("id_user")
  private BigInteger idUser;

  /**
   * Токен пользователя,
   * позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  @JsonProperty("resource_token")
  private BigInteger resourceToken;
}
