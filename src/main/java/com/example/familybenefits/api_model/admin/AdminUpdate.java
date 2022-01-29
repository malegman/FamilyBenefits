package com.example.familybenefits.api_model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

/**
 * Объект запроса для обновления администратора
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdate {

  /**
   * ID администратора
   */
  @JsonProperty("id")
  private BigInteger id;

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
}
