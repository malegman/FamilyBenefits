package com.example.familybenefits.api_model.common;

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
  private BigInteger idObject;

  /**
   * ID пользователя, который отправил запрос на удаление
   */
  private BigInteger idUser;

  /**
   * Токен пользователя,
   * позволяющий работать с ресурсами сервиса в рамках своего профиля
   */
  private BigInteger resourceToken;
}
