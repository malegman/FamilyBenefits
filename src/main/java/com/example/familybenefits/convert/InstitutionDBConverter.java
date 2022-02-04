package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "institution" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class InstitutionDBConverter {

  /**
   * Преобразует объект запроса на добавление учреждения в модель таблицы "institution", обрабатывая строковые поля для БД
   * @param institutionAdd объект запроса на добавление учреждения
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "institution"
   */
  static public InstitutionEntity fromAdd(InstitutionAdd institutionAdd, Function<String, String> prepareDBFunc) {

    if (institutionAdd == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .name(prepareDBFunc.apply(institutionAdd.getName()))
        .address(prepareDBFunc.apply(institutionAdd.getAddress()))
        .info(prepareDBFunc.apply(institutionAdd.getInfo()))
        .phone(prepareDBFunc.apply(institutionAdd.getPhone()))
        .schedule(prepareDBFunc.apply(institutionAdd.getSchedule()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(institutionAdd.getIdCity())))
        .benefitEntitySet(institutionAdd.getIdBenefitSet()
                              .stream()
                              .map(id -> new BenefitEntity(prepareDBFunc.apply(id)))
                              .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление учреждения в модель таблицы "institution", обрабатывая строковые поля для БД
   * @param institutionUpdate объект запроса на обновление учреждения
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "institution"
   */
  static public InstitutionEntity fromUpdate(InstitutionUpdate institutionUpdate, Function<String, String> prepareDBFunc) {

    if (institutionUpdate == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .id(prepareDBFunc.apply(institutionUpdate.getId()))
        .name(prepareDBFunc.apply(institutionUpdate.getName()))
        .address(prepareDBFunc.apply(institutionUpdate.getAddress()))
        .info(prepareDBFunc.apply(institutionUpdate.getInfo()))
        .phone(prepareDBFunc.apply(institutionUpdate.getPhone()))
        .schedule(prepareDBFunc.apply(institutionUpdate.getSchedule()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(institutionUpdate.getIdCity())))
        .benefitEntitySet(institutionUpdate.getIdBenefitSet()
                              .stream()
                              .map(id -> new BenefitEntity(prepareDBFunc.apply(id)))
                              .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "institution" в объект информации об учреждении
   * @param institutionEntity модель таблицы "institution"
   * @return информация об учреждении
   */
  static public InstitutionInfo toInfo(InstitutionEntity institutionEntity) {

    if (institutionEntity == null) {
      return new InstitutionInfo();
    }

    return InstitutionInfo
        .builder()
        .id(institutionEntity.getId())
        .name(institutionEntity.getName())
        .info(institutionEntity.getInfo())
        .address(institutionEntity.getAddress())
        .phone(institutionEntity.getPhone())
        .email(institutionEntity.getEmail())
        .schedule(institutionEntity.getSchedule())
        .shortCity(CityDBConverter.toShortInfo(institutionEntity.getCityEntity()))
        .shortBenefitSet(institutionEntity.getBenefitEntitySet()
                             .stream()
                             .map(BenefitDBConverter::toShortInfo)
                             .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "institution" в объект краткой информации об объекте
   * @param institutionEntity модель таблицы "institution"
   * @return краткая информация об учреждении
   */
  static public ObjectShortInfo toShortInfo(InstitutionEntity institutionEntity) {

    if (institutionEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(institutionEntity.getId())
        .nameObject(institutionEntity.getName())
        .build();
  }
}
