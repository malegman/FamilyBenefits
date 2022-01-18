package com.example.familybenefits.api_model.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;

/**
 * Объект запроса для изменения пароля пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {

  /**
   * Старый пароль пользователя
   */
  @NonNull
  @JsonProperty("old_password")
  private String oldPassword;

  /**
   * Новый пароль пользователя
   */
  @NonNull
  @JsonProperty("new_password")
  private String newPassword;

  /**
   * Повторно введенный новый пароль пользователя
   */
  @NonNull
  @JsonProperty("repeat_password")
  private String repeatPassword;
}
