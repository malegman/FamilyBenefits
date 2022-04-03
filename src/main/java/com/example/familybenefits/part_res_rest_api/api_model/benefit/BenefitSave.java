package com.example.familybenefits.part_res_rest_api.api_model.benefit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект запроса для сохранения пособия, создания или обновления
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitSave {

  /**
   * Название пособия
   */
  @JsonProperty("name")
  private String name;

  /**
   * Информация пособия
   */
  @JsonProperty("info")
  private String info;

  /**
   * Документы для получения пособия
   */
  @JsonProperty("documents")
  private String documents;

  /**
   * Список ID городов пособия
   */
  @JsonProperty("idCityList")
  private List<String> idCityList;

  /**
   * Список ID критерий пособия
   */
  @JsonProperty("idCriterionList")
  private List<String> idCriterionList;

  /**
   * Список ID учреждений пособия
   */
  @JsonProperty("idInstitutionList")
  private List<String> idInstitutionList;
}
