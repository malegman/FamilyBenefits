package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "benefit" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class BenefitDBConverter {

  /**
   * Преобразует объект запроса на добавление пособия в модель таблицы "benefit", обрабатывая строковые поля для БД
   * @param benefitAdd объект запроса на добавление пособия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "benefit"
   */
  static public BenefitEntity fromAdd(BenefitAdd benefitAdd, Function<String, String> prepareDBFunc) {

    if (benefitAdd == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .name(prepareDBFunc.apply(benefitAdd.getName()))
        .info(prepareDBFunc.apply(benefitAdd.getInfo()))
        .documents(prepareDBFunc.apply(benefitAdd.getDocuments()))
        .cityEntitySet(benefitAdd.getIdCitySet()
                           .stream()
                           .map(id -> new CityEntity(prepareDBFunc.apply(id)))
                           .collect(Collectors.toSet()))
        .institutionEntitySet(benefitAdd.getIdInstitutionSet()
                                  .stream()
                                  .map(id -> new InstitutionEntity(prepareDBFunc.apply(id)))
                                  .collect(Collectors.toSet()))
        .criterionEntitySet(benefitAdd.getIdCriterionSet()
                                .stream()
                                .map(id -> new CriterionEntity(prepareDBFunc.apply(id)))
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление пособия в модель таблицы "benefit", обрабатывая строковые поля для БД
   * @param benefitUpdate объект запроса на обновление пособия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "benefit"
   */
  static public BenefitEntity fromUpdate(BenefitUpdate benefitUpdate, Function<String, String> prepareDBFunc) {

    if (benefitUpdate == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .id(prepareDBFunc.apply(benefitUpdate.getId()))
        .name(prepareDBFunc.apply(benefitUpdate.getName()))
        .info(prepareDBFunc.apply(benefitUpdate.getInfo()))
        .documents(prepareDBFunc.apply(benefitUpdate.getDocuments()))
        .cityEntitySet(benefitUpdate.getIdCitySet()
                           .stream()
                           .map(id -> new CityEntity(prepareDBFunc.apply(id)))
                           .collect(Collectors.toSet()))
        .institutionEntitySet(benefitUpdate.getIdInstitutionSet()
                                  .stream()
                                  .map(id -> new InstitutionEntity(prepareDBFunc.apply(id)))
                                  .collect(Collectors.toSet()))
        .criterionEntitySet(benefitUpdate.getIdCriterionSet()
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
        .shortCitySet(benefitEntity.getCityEntitySet()
                          .stream()
                          .map(CityDBConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .shortInstitutionSet(benefitEntity.getInstitutionEntitySet()
                                 .stream()
                                 .map(InstitutionDBConverter::toShortInfo)
                                 .collect(Collectors.toSet()))
        .criterionSet(benefitEntity.getCriterionEntitySet()
                          .stream()
                          .map(CriterionDBConverter::toInfo)
                          .collect(Collectors.toSet()))
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
