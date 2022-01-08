package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Информация об администраторе
 */
@Data
public class AdminInfo {

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
   * Краткая информация о городе
   */
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;

  /**
   * Дата регистрации администратора
   */
  @JsonProperty("date_registration")
  private String dateRegistration;
}
