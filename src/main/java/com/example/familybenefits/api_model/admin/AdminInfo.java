package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Информация об администраторе
 */
@Data
@Builder
public class AdminInfo {

  /**
   * ID администратора
   */
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя администратора
   */
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта администратора
   */
  @JsonProperty("email")
  private String email;

  /**
   * Статус почты, true, если подтверждена
   */
  @JsonProperty("is_verified_email")
  private boolean isVerifiedEmail;

  /**
   * Название города администратора
   */
  @JsonProperty("name_city")
  private ShortObjectInfo nameCity;

  /**
   * Дата регистрации администратора
   */
  @JsonProperty("date_registration")
  private String dateRegistration;
}
