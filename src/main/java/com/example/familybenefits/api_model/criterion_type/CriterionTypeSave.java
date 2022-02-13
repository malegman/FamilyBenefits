package com.example.familybenefits.api_model.criterion_type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Объект запроса для сохранения типа критерия, создания или обновления
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionTypeSave {

  /**
   * Название типа критерия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация типа критерия
   */
  @JsonProperty("info")
  private String info;
}
