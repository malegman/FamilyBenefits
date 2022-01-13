package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект ответа на вход в систему
 */
@Data
@Builder
public class LoginResponse {

  /**
   * ID пользователя
   */
  @JsonProperty("id_user")
  private BigInteger idUser;

  /**
   * Имя пользователя
   */
  @JsonProperty("name_user")
  private String nameUser;

  /**
   * Множество ролей пользователя
   */
  @JsonProperty("role_user_set")
  private Set<String> roleUserSet;

  /**
   * Токен доступа в формате jwt
   */
  @JsonProperty("jwt")
  private String jwt;
}
