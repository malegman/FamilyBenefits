package com.example.familybenefits.api_model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
  @JsonProperty("id")
  private String id;

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
  @JsonProperty("isVerifiedEmail")
  private boolean isVerifiedEmail;

  /**
   * Множество названий ролей администратора
   */
  @JsonProperty("nameRoleSet")
  private Set<String> nameRoleSet;
}
