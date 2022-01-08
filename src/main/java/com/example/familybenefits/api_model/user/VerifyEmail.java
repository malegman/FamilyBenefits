package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект для подтверждения почты
 */
@Data
public class VerifyEmail {

  /**
   * Данные сессии пользователя для доступа к ресурсам
   */
  @JsonProperty("session_access")
  private SessionAccess sessionAccess;

  /**
   * Код пользователя, необходимый для подтверждения почты
   */
  @JsonProperty("verify_email_code")
  private BigInteger verifyEmailCode;
}
