package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "criterion_type" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionTypeDBConverter {

  /**
   * Преобразует объект запроса на сохранение типа критерия в модель таблицы "criterion_type", обрабатывая строковые поля для БД
   * @param criterionTypeSave объект запроса на сохранение типа критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionTypeEntity fromSave(CriterionTypeSave criterionTypeSave, Function<String, String> prepareDBFunc) {

    if (criterionTypeSave == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .name(prepareDBFunc.apply(criterionTypeSave.getName()))
        .info(prepareDBFunc.apply(criterionTypeSave.getInfo()))
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion_type" в объект информации о типе критерия
   * @param criterionTypeEntity модель таблицы "criterion_type"
   * @return информация о типе критерия
   */
  static public CriterionTypeInfo toInfo(CriterionTypeEntity criterionTypeEntity) {

    if (criterionTypeEntity == null) {
      return new CriterionTypeInfo();
    }

    return CriterionTypeInfo
        .builder()
        .id(criterionTypeEntity.getId())
        .name(criterionTypeEntity.getName())
        .info(criterionTypeEntity.getInfo())
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion_type" в объект краткой информации об объекте
   * @param criterionTypeEntity модель таблицы "criterion_type"
   * @return краткая информация о типе критерия
   */
  static public ObjectShortInfo toShortInfo(CriterionTypeEntity criterionTypeEntity) {

    if (criterionTypeEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(criterionTypeEntity.getId())
        .nameObject(criterionTypeEntity.getName())
        .build();
  }
}
