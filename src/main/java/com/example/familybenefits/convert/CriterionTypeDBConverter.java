package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "criterion_type" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionTypeDBConverter {

  /**
   * Преобразует объект запроса на добавление типа критерия в модель таблицы "criterion_type", обрабатывая строковые поля для БД
   * @param criterionTypeAdd объект запроса на добавление типа критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionTypeEntity fromAdd(CriterionTypeAdd criterionTypeAdd, Function<String, String> prepareDBFunc) {

    if (criterionTypeAdd == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .name(prepareDBFunc.apply(criterionTypeAdd.getName()))
        .info(prepareDBFunc.apply(criterionTypeAdd.getInfo()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление типа критерия в модель таблицы "criterion_type", обрабатывая строковые поля для БД
   * @param criterionTypeUpdate объект запроса на обновление типа критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionTypeEntity fromUpdate(CriterionTypeUpdate criterionTypeUpdate, Function<String, String> prepareDBFunc) {

    if (criterionTypeUpdate == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .id(prepareDBFunc.apply(criterionTypeUpdate.getId()))
        .name(prepareDBFunc.apply(criterionTypeUpdate.getName()))
        .info(prepareDBFunc.apply(criterionTypeUpdate.getInfo()))
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
        .shortCriterionSet(criterionTypeEntity.getCriterionEntitySet()
                               .stream()
                               .map(CriterionDBConverter::toShortInfo)
                               .collect(Collectors.toSet()))
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
