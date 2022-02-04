package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "city" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CityDBConverter {

  /**
   * Преобразует объект запроса на добавление города в модель таблицы "city", обрабатывая строковые поля для БД
   * @param cityAdd объект запроса на добавление города
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "city"
   */
  static public CityEntity fromAdd(CityAdd cityAdd, Function<String, String> prepareDBFunc) {

    if (cityAdd == null) {
      return new CityEntity();
    }

    return CityEntity
        .builder()
        .name(prepareDBFunc.apply(cityAdd.getName()))
        .info(prepareDBFunc.apply(cityAdd.getInfo()))
        .benefitEntitySet(cityAdd.getIdBenefitSet()
                              .stream()
                              .map(id -> new BenefitEntity(prepareDBFunc.apply(id)))
                              .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление города в модель таблицы "city", обрабатывая строковые поля для БД
   * @param cityUpdate объект запроса на обновление города
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "city"
   */
  static public CityEntity fromUpdate(CityUpdate cityUpdate, Function<String, String> prepareDBFunc) {

    if (cityUpdate == null) {
      return new CityEntity();
    }

    return CityEntity
        .builder()
        .id(prepareDBFunc.apply(cityUpdate.getId()))
        .name(prepareDBFunc.apply(cityUpdate.getName()))
        .info(prepareDBFunc.apply(cityUpdate.getInfo()))
        .benefitEntitySet(cityUpdate.getIdBenefitSet()
                              .stream()
                              .map(id -> new BenefitEntity(prepareDBFunc.apply(id)))
                              .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "city" в объект информации о городе
   * @param cityEntity модель таблицы "city"
   * @return информация о городе
   */
  static public CityInfo toInfo(CityEntity cityEntity) {

    if (cityEntity == null) {
      return new CityInfo();
    }

    return CityInfo
        .builder()
        .id(cityEntity.getId())
        .name(cityEntity.getName())
        .info(cityEntity.getInfo())
        .shortBenefitSet(cityEntity.getBenefitEntitySet()
                              .stream()
                              .map(BenefitDBConverter::toShortInfo)
                              .collect(Collectors.toSet()))
        .shortInstitutionSet(cityEntity.getInstitutionEntitySet()
                              .stream()
                              .map(InstitutionDBConverter::toShortInfo)
                              .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "city" в объект краткой информации об объекте
   * @param cityEntity модель таблицы "city"
   * @return краткая информация о городе
   */
  static public ObjectShortInfo toShortInfo(CityEntity cityEntity) {

    if (cityEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(cityEntity.getId())
        .nameObject(cityEntity.getName())
        .build();
  }
}
