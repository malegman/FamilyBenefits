package com.example.familybenefits.api_model.user;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @JsonProperty("id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @JsonProperty("name")
  private String name;

  /**
   * Электронная почта пользователя
   */
  @JsonProperty("email")
  private String email;

  /**
   * Статус почты, true, если подтверждена
   */
  @JsonProperty("isVerifiedEmail")
  private boolean isVerifiedEmail;

  /**
   * Множество дат рождений детей пользователя
   */
  @JsonProperty("birthDateChildren")
  private Set<String> birthDateChildren;

  /**
   * Дата рождения пользователя
   */
  @JsonProperty("dateBirth")
  private String dateBirth;

  /**
   * Множество названий ролей пользователя
   */
  @JsonProperty("nameRoleSet")
  private Set<String> nameRoleSet;

  /**
   * Краткая информация о городе пользователя
   */
  @JsonProperty("shortCity")
  private ObjectShortInfo shortCity;

  /**
   * Множество информаций о критериях пользователя
   */
  @JsonProperty("criterionSet")
  private Set<CriterionInfo> criterionSet;
}
