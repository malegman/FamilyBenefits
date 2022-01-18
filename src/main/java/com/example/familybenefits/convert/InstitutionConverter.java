package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;

/**
 * Класс преобразования модели таблицы "institution" в другие объекты и получения из других объектов
 */
public class InstitutionConverter {

  /**
   * Преобразует объект запроса на добавление учреждения в модель таблицы "institution"
   * @param institutionAdd объект запроса на добавление учреждения
   * @return модель таблицы "institution"
   */
  static public InstitutionEntity fromAdd(InstitutionAdd institutionAdd) {

    if (institutionAdd == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .name(institutionAdd.getName())
        .address(institutionAdd.getAddress())
        .info(institutionAdd.getInfo())
        .phone(institutionAdd.getPhone())
        .schedule(institutionAdd.getSchedule())
        .cityEntity(new CityEntity(institutionAdd.getIdCity()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление учреждения в модель таблицы "institution"
   * @param institutionUpdate объект запроса на обновление учреждения
   * @return модель таблицы "institution"
   */
  static public InstitutionEntity fromUpdate(InstitutionUpdate institutionUpdate) {

    if (institutionUpdate == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .id(institutionUpdate.getId())
        .name(institutionUpdate.getName())
        .address(institutionUpdate.getAddress())
        .phone(institutionUpdate.getPhone())
        .email(institutionUpdate.getEmail())
        .schedule(institutionUpdate.getSchedule())
        .cityEntity(new CityEntity(institutionUpdate.getId()))
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
   * Преобразует модель таблицы "institutionEntity" в объект краткой информации об объекте
   * @param institutionEntity модель таблицы "institutionEntity"
   * @return краткая информция об учреждении
   */
  static public ObjectShortInfo toShortObjectInfo(InstitutionEntity institutionEntity) {

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