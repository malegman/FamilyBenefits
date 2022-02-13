package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitSave;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "benefit" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class BenefitDBConverter {

  /**
   * Преобразует объект запроса на сохранение пособия в модель таблицы "benefit", обрабатывая строковые поля для БД
   * @param benefitSave объект запроса на сохранение пособия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "benefit"
   */
  static public BenefitEntity fromSave(BenefitSave benefitSave, Function<String, String> prepareDBFunc) {

    if (benefitSave == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .name(prepareDBFunc.apply(benefitSave.getName()))
        .info(prepareDBFunc.apply(benefitSave.getInfo()))
        .documents(prepareDBFunc.apply(benefitSave.getDocuments()))
        .cityEntitySet(benefitSave.getIdCitySet()
                           .stream()
                           .map(id -> new CityEntity(prepareDBFunc.apply(id)))
                           .collect(Collectors.toSet()))
        .criterionEntitySet(benefitSave.getIdCriterionSet()
                                .stream()
                                .map(id -> new CriterionEntity(prepareDBFunc.apply(id)))
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "benefit" в объект информации о пособии
   * @param benefitEntity модель таблицы "benefit"
   * @return информация о пособии
   */
  static public BenefitInfo toInfo(BenefitEntity benefitEntity) {

    if (benefitEntity == null) {
      return new BenefitInfo();
    }

    return BenefitInfo
        .builder()
        .id(benefitEntity.getId())
        .name(benefitEntity.getName())
        .info(benefitEntity.getInfo())
        .documents(benefitEntity.getDocuments())
        .build();
  }

  /**
   * Преобразует модель таблицы "benefit в объект краткой информации об объекте
   * @param benefitEntity модель таблицы "benefit"
   * @return краткая информация о пособии
   */
  static public ObjectShortInfo toShortInfo(BenefitEntity benefitEntity) {

    if (benefitEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(benefitEntity.getId())
        .nameObject(benefitEntity.getName())
        .build();
  }
}
