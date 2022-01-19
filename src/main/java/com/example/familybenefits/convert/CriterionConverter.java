package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

/**
 * Класс преобразования модели таблицы "criterion" в другие объекты и получения из других объектов
 */
public class CriterionConverter {

  /**
   * Преобразует объект запроса на добавление критерия в модель таблицы "criterion"
   * @param criterionAdd объект запроса на добавление критерия
   * @return модель таблицы "criterion_type"
   */
  static public CriterionEntity fromAdd(CriterionAdd criterionAdd) {

    if (criterionAdd == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .name(criterionAdd.getName())
        .info(criterionAdd.getInfo())
        .criterionType(
            criterionAdd.getIdCriterionType() == null
                ? null
                : new CriterionTypeEntity(criterionAdd.getIdCriterionType()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление критерия в модель таблицы "criterion"
   * @param criterionUpdate объект запроса на обновление критерия
   * @return модель таблицы "criterion_type"
   */
  static public CriterionEntity fromUpdate(CriterionUpdate criterionUpdate) {

    if (criterionUpdate == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .id(criterionUpdate.getId())
        .name(criterionUpdate.getName())
        .info(criterionUpdate.getInfo())
        .criterionType(
            criterionUpdate.getIdCriterionType() == null
                ? null
                : new CriterionTypeEntity(criterionUpdate.getIdCriterionType()))
        .build();
  }

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

  /**
   * Преобразует модель таблицы "criterion" в объект краткой информации об объекте
   * @param criterionEntity модель таблицы "criterion"
   * @return краткая информция о критерии
   */
  static public ObjectShortInfo toShortInfo(CriterionEntity criterionEntity) {

    if (criterionEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(criterionEntity.getId())
        .nameObject(criterionEntity.getName())
        .build();
  }
}
