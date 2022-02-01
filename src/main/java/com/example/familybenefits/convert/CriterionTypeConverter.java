package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;

import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "criterion_type" в другие объекты и получения из других объектов
 */
public class CriterionTypeConverter {

  /**
   * Преобразует объект запроса на добавление типа критерия в модель таблицы "criterion_type"
   * @param criterionTypeAdd объект запроса на добавление типа критерия
   * @return модель таблицы "criterion_type"
   */
  static public CriterionTypeEntity fromAdd(CriterionTypeAdd criterionTypeAdd) {

    if (criterionTypeAdd == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .name(criterionTypeAdd.getName())
        .info(criterionTypeAdd.getInfo())
        .build();
  }

  /**
   * Преобразует объект запроса на обновление типа критерия в модель таблицы "criterion_type"
   * @param criterionTypeUpdate объект запроса на обновление типа критерия
   * @return модель таблицы "criterion_type"
   */
  static public CriterionTypeEntity fromUpdate(CriterionTypeUpdate criterionTypeUpdate) {

    if (criterionTypeUpdate == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .id(criterionTypeUpdate.getId())
        .name(criterionTypeUpdate.getName())
        .info(criterionTypeUpdate.getInfo())
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
                               .map(CriterionConverter::toShortInfo)
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
