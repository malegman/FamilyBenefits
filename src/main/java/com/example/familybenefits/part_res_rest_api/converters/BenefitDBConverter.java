package com.example.familybenefits.part_res_rest_api.converters;

import com.example.familybenefits.dto.entities.BenefitEntity;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInfo;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitSave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.security.RandomValue;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "benefit" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class BenefitDBConverter {

  /**
   * Преобразует объект запроса на сохранение пособия в модель таблицы "benefit", обрабатывая строковые поля для БД
   * @param idBenefit ID пособия. Если {@code null}, значение ID генерируется.
   * @param benefitSave объект запроса на сохранение пособия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "benefit"
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  public static BenefitEntity fromSave(String idBenefit, BenefitSave benefitSave, Function<String, String> prepareDBFunc) throws InvalidStringException {

    if (benefitSave == null) {
      return new BenefitEntity();
    }

    return BenefitEntity
        .builder()
        .id(idBenefit != null
                ? idBenefit
                : RandomValue.randomString(R.ID_LENGTH))
        .name(prepareDBFunc.apply(FieldConverter.withSymbolsField(benefitSave.getName(), "name", true)))
        .info(prepareDBFunc.apply(FieldConverter.withSymbolsField(benefitSave.getInfo(), "info", true)))
        .documents(prepareDBFunc.apply(benefitSave.getDocuments()))
        .build();
  }

  /**
   * Преобразует модель таблицы "benefit" в объект информации о пособии
   * @param benefitEntity модель таблицы "benefit"
   * @return информация о пособии
   */
  public static BenefitInfo toInfo(BenefitEntity benefitEntity) {

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
  public static ObjectShortInfo toShortInfo(BenefitEntity benefitEntity) {

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
