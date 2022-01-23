package com.example.familybenefits.api_model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Информация об администраторе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
