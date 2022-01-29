package com.example.familybenefits.api_model.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Объект ответа на вход в систему
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  /**
   * ID пользователя
   */
  @JsonProperty("idUser")
  private BigInteger idUser;

  /**
   * Имя пользователя
   */
  @JsonProperty("nameUser")
  private String nameUser;

  /**
   * Множество названий ролей пользователя
   */
  @JsonProperty("nameRoleUserSet")
  private Set<String> nameRoleUserSet;

  /**
   * Токен доступа в формате jwt
   */
  @JsonProperty("jwt")
  private String jwt;
}