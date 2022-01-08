package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * Данные сессии пользователя для доступа к ресурсам
 */
@Data
public class SessionAccess {

  /**
   * ID пользователя
   */
  @JsonProperty("id_user")
  private BigInteger idUser;

  /**
   * Токен доступа jwt
   */
  @JsonProperty("jwt")
  private String jwt;

  /**
   * Токен восстановления токена доступа
   */
  @JsonProperty("refresh_token")
  private BigInteger refreshToken;
}
