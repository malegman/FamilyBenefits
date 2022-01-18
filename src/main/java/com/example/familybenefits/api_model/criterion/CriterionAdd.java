package com.example.familybenefits.api_model.criterion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

/**
 * Объект запроса для добавления критерия
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionAdd {

  /**
   * Название критерия
   */
  @NonNull
  @JsonProperty("name")
  private String name;

  /**
   * Информация критерия
   */
  @NonNull
  @JsonProperty("info")
  private String info;

  /**
   * ID типа критерия критерия
   */
  @Nullable
  @JsonProperty("id_criterion_type")
  private BigInteger idCriterionType;
}
