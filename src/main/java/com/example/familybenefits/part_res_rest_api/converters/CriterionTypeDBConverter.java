package com.example.familybenefits.part_res_rest_api.converters;

import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.dto.entities.CriterionTypeEntity;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.security.RandomValue;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "criterion_type" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionTypeDBConverter {

  /**
   * Преобразует объект запроса на сохранение типа критерия в модель таблицы "criterion_type", обрабатывая строковые поля для БД
   * @param idCriterionType ID типа критерия. Если {@code null}, значение ID генерируется.
   * @param criterionTypeSave объект запроса на сохранение типа критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  public static CriterionTypeEntity fromSave(String idCriterionType, CriterionTypeSave criterionTypeSave, Function<String, String> prepareDBFunc) throws InvalidStringException {

    if (criterionTypeSave == null) {
      return new CriterionTypeEntity();
    }

    return CriterionTypeEntity
        .builder()
        .id(idCriterionType != null
                ? idCriterionType
                : RandomValue.randomString(R.ID_LENGTH))
        .name(prepareDBFunc.apply(FieldConverter.withSymbolsField(criterionTypeSave.getName(), "name", true)))
        .info(prepareDBFunc.apply(FieldConverter.withSymbolsField(criterionTypeSave.getInfo(), "info", true)))
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion_type" в объект информации о типе критерия
   * @param criterionTypeEntity модель таблицы "criterion_type"
   * @return информация о типе критерия
   */
  public static CriterionTypeInfo toInfo(CriterionTypeEntity criterionTypeEntity) {

    if (criterionTypeEntity == null) {
      return new CriterionTypeInfo();
    }

    return CriterionTypeInfo
        .builder()
        .id(criterionTypeEntity.getId())
        .name(criterionTypeEntity.getName())
        .info(criterionTypeEntity.getInfo())
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion_type" в объект краткой информации об объекте
   * @param criterionTypeEntity модель таблицы "criterion_type"
   * @return краткая информация о типе критерия
   */
  public static ObjectShortInfo toShortInfo(CriterionTypeEntity criterionTypeEntity) {

    if (criterionTypeEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(criterionTypeEntity.getId())
        .nameObject(criterionTypeEntity.getName())
        .build();
  }
}
