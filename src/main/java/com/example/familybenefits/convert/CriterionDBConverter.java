package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "criterion" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionDBConverter {

  /**
   * Преобразует объект запроса на добавление критерия в модель таблицы "criterion", обрабатывая строковые поля для БД
   * @param criterionAdd объект запроса на добавление критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionEntity fromAdd(CriterionAdd criterionAdd, Function<String, String> prepareDBFunc) {

    if (criterionAdd == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .name(prepareDBFunc.apply(criterionAdd.getName()))
        .info(prepareDBFunc.apply(criterionAdd.getInfo()))
        .criterionTypeEntity(new CriterionTypeEntity(prepareDBFunc.apply(criterionAdd.getIdCriterionType())))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление критерия в модель таблицы "criterion", обрабатывая строковые поля для БД
   * @param criterionUpdate объект запроса на обновление критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   */
  static public CriterionEntity fromUpdate(CriterionUpdate criterionUpdate, Function<String, String> prepareDBFunc) {

    if (criterionUpdate == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .id(prepareDBFunc.apply(criterionUpdate.getId()))
        .name(prepareDBFunc.apply(criterionUpdate.getName()))
        .info(prepareDBFunc.apply(criterionUpdate.getInfo()))
        .criterionTypeEntity(new CriterionTypeEntity(prepareDBFunc.apply(criterionUpdate.getIdCriterionType())))
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
        .shortCriterionType(CriterionTypeDBConverter.toShortInfo(criterionEntity.getCriterionTypeEntity()))
        .shortBenefitSet(criterionEntity.getBenefitEntitySet()
                             .stream()
                             .map(BenefitDBConverter::toShortInfo)
                             .collect(Collectors.toSet()))
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
