package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

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
  @NonNull
  @JsonProperty("id_user")
  private BigInteger idUser;

  /**
   * Имя пользователя
   */
  @NonNull
  @JsonProperty("name_user")
  private String nameUser;

  /**
   * Множество ролей пользователя
   */
  @NonNull
  @JsonProperty("role_user_set")
  private Set<String> roleUserSet;

  /**
   * Токен доступа в формате jwt
   */
  @NonNull
  @JsonProperty("jwt")
  private String jwt;
}
