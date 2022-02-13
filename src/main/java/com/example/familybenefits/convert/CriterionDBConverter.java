package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionSave;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "criterion" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionDBConverter {

  /**
   * Преобразует объект запроса на сохранение критерия в модель таблицы "criterion", обрабатывая строковые поля для БД
   * @param criterionSave объект запроса на сохранение критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionEntity fromSave(CriterionSave criterionSave, Function<String, String> prepareDBFunc) {

    if (criterionSave == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .name(prepareDBFunc.apply(criterionSave.getName()))
        .info(prepareDBFunc.apply(criterionSave.getInfo()))
        .criterionTypeEntity(new CriterionTypeEntity(prepareDBFunc.apply(criterionSave.getIdCriterionType())))
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
        .nameCriterionType(criterionEntity.getCriterionTypeEntity().getName())
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion" в объект краткой информации об объекте
   * @param criterionEntity модель таблицы "criterion"
   * @return краткая информация о критерии
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
