package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.dao.entity.InstitutionEntity;

/**
 * Класс преобразования модели таблицы "institution" в другие объекты и получения из других объектов
 */
public class InstitutionConverter {

  /**
   * Преобразование модели таблицы "institution" в объект информации об учреждении
   * @param institutionEntity модель таблицы "institution"
   * @return информация об учреждении
   */
  static public InstitutionInfo toInfo(InstitutionEntity institutionEntity) {

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
}
