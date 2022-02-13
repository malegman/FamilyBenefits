package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionSave;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
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
   * Преобразует объект запроса на сохранение учреждения в модель таблицы "institution", обрабатывая строковые поля для БД
   * @param institutionSave объект запроса на сохранение учреждения
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "institution"
   */
  static public InstitutionEntity fromSave(InstitutionSave institutionSave, Function<String, String> prepareDBFunc) {

    if (institutionSave == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .name(prepareDBFunc.apply(institutionSave.getName()))
        .address(prepareDBFunc.apply(institutionSave.getAddress()))
        .info(prepareDBFunc.apply(institutionSave.getInfo()))
        .phone(prepareDBFunc.apply(institutionSave.getPhone()))
        .email(prepareDBFunc.apply(institutionSave.getEmail()))
        .schedule(prepareDBFunc.apply(institutionSave.getSchedule()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(institutionSave.getIdCity())))
        .benefitEntitySet(institutionSave.getIdBenefitSet()
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
        .nameCity(institutionEntity.getCityEntity().getName())
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
