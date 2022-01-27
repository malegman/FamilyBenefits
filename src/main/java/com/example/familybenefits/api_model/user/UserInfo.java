package com.example.familybenefits.api_model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
import java.util.Set;

/**
 * Информация о пользователе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

  /**
   * ID пользователя
   */
  @NonNull
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта пользователя
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
   * Название города пользователя
   */
  @Nullable
  @JsonProperty("name_city")
  private String nameCity;

  /**
   * Множество дат рождений детей пользователя
   */
  @NonNull
  @JsonProperty("birth_date_children")
  private Set<String> birthDateChildren;

  /**
   * Дата рождения пользователя
   */
  @NonNull
  @JsonProperty("date_birth")
  private String dateBirth;

  /**
   * Множество названий ролей пользователя
   */
  @JsonProperty("name_role_set")
  private Set<String> nameRoleSet;
}
