package com.example.familybenefits.api_model.criterion_type;

import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Информация о типе критерия
 */
@Data
public class CriterionTypeInfo {

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

  /**
   * Список кратких информаций о критериях
   */
  @JsonProperty("short_criterion_list")
  private List<ShortObjectInfo> shortCriterionList;
}
