package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Объект запроса на сброс сессии пользователя
 */
@Data
public class ResetSession {

  /**
   * ID пользователя
   */
  @JsonProperty("id_user")
  private BigInteger idUser;

  /**
   * Код сброса сессии
   */
  @JsonProperty("reset_code")
  private BigInteger resetCode;
}

