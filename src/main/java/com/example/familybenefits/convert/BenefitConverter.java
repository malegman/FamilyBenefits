package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;

import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "benefit" в другие объекты и получения из других объектов
 */
public class BenefitConverter {

  /**
   * Преобразует объект запроса на добавление пособия в модель таблицы "benefit"
   * @param benefitAdd объект запроса на добавление пособия
   * @return модель таблицы "benefit"
   */
  static public BenefitEntity fromAdd(BenefitAdd benefitAdd) {

    if (benefitAdd == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .name(benefitAdd.getName())
        .info(benefitAdd.getInfo())
        .documents(benefitAdd.getDocuments())
        .cityEntitySet(benefitAdd.getIdCitySet()
                           .stream()
                           .map(CityEntity::new)
                           .collect(Collectors.toSet()))
        .institutionEntitySet(benefitAdd.getIdInstitutionSet()
                                  .stream()
                                  .map(InstitutionEntity::new)
                                  .collect(Collectors.toSet()))
        .criterionEntitySet(benefitAdd.getIdCriterionSet()
                                .stream()
                                .map(CriterionEntity::new)
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление пособия в модель таблицы "benefit"
   * @param benefitUpdate объект запроса на обновление пособия
   * @return модель таблицы "benefit"
   */
  static public BenefitEntity fromUpdate(BenefitUpdate benefitUpdate) {

    if (benefitUpdate == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .id(benefitUpdate.getId())
        .name(benefitUpdate.getName())
        .info(benefitUpdate.getInfo())
        .documents(benefitUpdate.getDocuments())
        .cityEntitySet(benefitUpdate.getIdCitySet()
                           .stream()
                           .map(CityEntity::new)
                           .collect(Collectors.toSet()))
        .institutionEntitySet(benefitUpdate.getIdInstitutionSet()
                                  .stream()
                                  .map(InstitutionEntity::new)
                                  .collect(Collectors.toSet()))
        .criterionEntitySet(benefitUpdate.getIdCriterionSet()
                                .stream()
                                .map(CriterionEntity::new)
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
                          .map(CityConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .shortInstitutionSet(benefitEntity.getInstitutionEntitySet()
                                 .stream()
                                 .map(InstitutionConverter::toShortInfo)
                                 .collect(Collectors.toSet()))
        .criterionSet(benefitEntity.getCriterionEntitySet()
                          .stream()
                          .map(CriterionConverter::toInfo)
                          .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "benefit в объект краткой информации об объекте
   * @param benefitEntity модель таблицы "benefit"
   * @return краткая информция о пособии
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
