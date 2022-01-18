package com.example.familybenefits.api_model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Информация об администраторе
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AdminInfo {

  /**
   * ID администратора
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя администратора
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта администратора
   */
  @NonNull
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
  @Nullable
  @JsonProperty("name_city")
  private String nameCity;

  /**
   * Дата регистрации администратора
   */
  @NonNull
  @JsonProperty("date_registration")
  private String dateRegistration;
}
