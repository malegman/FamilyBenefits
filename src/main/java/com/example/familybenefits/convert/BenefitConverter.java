package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.dao.entity.BenefitEntity;

/**
 * Класс преобразования модели таблицы "benefit" в другие объекты и получения из других объектов
 */
public class BenefitConverter {

  /**
   * Преобразование модели таблицы "benefit" в объект информации о пособии
   * @param benefitEntity модель таблицы "benefit"
   * @return информация о пособии
   */
  static public BenefitInfo toInfo(BenefitEntity benefitEntity) {

    return BenefitInfo
        .builder()
        .id(benefitEntity.getId())
        .name(benefitEntity.getName())
        .info(benefitEntity.getInfo())
        .documents(benefitEntity.getDocuments())
        .build();
  }
}
