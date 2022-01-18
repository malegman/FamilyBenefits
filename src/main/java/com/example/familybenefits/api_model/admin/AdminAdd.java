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
 * Объект запроса для добавления администратора
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AdminAdd {

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
   * Пароль администратора
   */
  @NonNull
  @JsonProperty("password")
  private String password;

  /**
   * Повторно введенный пароль администратора
   */
  @NonNull
  @JsonProperty("repeat_password")
  private String repeatPassword;

  /**
   * ID города администратора
   */
  @Nullable
  @JsonProperty("id_city")
  private BigInteger idCity;
}
