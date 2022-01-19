package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.dao.entity.CriterionEntity;

/**
 * Класс преобразования модели таблицы "criterion" в другие объекты и получения из других объектов
 */
public class CriterionConverter {

  /**
   * Преобразует модель таблицы "criterion" в объект информации о критерии
   * @param criterionEntity модель таблицы "criterion"
   * @return информация о критерии
   */
  static public CriterionInfo toInfo(CriterionEntity criterionEntity) {

    if (criterionEntity == null) {
      return new CriterionInfo();
    }

    return CriterionInfo
        .builder()
        .id(criterionEntity.getId())
        .name(criterionEntity.getName())
        .info(criterionEntity.getInfo())
        .build();
  }
}
