package com.example.familybenefits.api_model.admin;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Объект запроса для добавления администратора
 */
@Data
@Builder
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
   * Краткая информация о городе администратора
   */
  @Nullable
  @JsonProperty("short_city")
  private ShortObjectInfo shortCity;
}
