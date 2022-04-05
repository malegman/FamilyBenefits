package com.example.familybenefits.part_res_rest_api.converters;

import com.example.familybenefits.dto.entities.CriterionEntity;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionSave;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.security.RandomValue;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "criterion" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class CriterionDBConverter {

  /**
   * Преобразует объект запроса на сохранение критерия в модель таблицы "criterion", обрабатывая строковые поля для БД
   * @param idCriterion ID критерия. Если {@code null}, значение ID генерируется.
   * @param criterionSave объект запроса на сохранение критерия
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "criterion_type"
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  public static CriterionEntity fromSave(String idCriterion, CriterionSave criterionSave, Function<String, String> prepareDBFunc) throws InvalidStringException {

    if (criterionSave == null) {
      return new CriterionEntity();
    }

    return CriterionEntity
        .builder()
        .id(idCriterion != null
                ? idCriterion
                : RandomValue.randomString(R.ID_LENGTH))
        .name(prepareDBFunc.apply(FieldConverter.withSymbolsField(criterionSave.getName(), "name", true)))
        .info(prepareDBFunc.apply(FieldConverter.withSymbolsField(criterionSave.getInfo(), "info", true)))
        .idCriterionType(criterionSave.getIdCriterionType())
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion" в объект информации о критерии
   * @param criterionEntity модель таблицы "criterion"
   * @param nameCriterionType название типа критерия
   * @return информация о критерии
   */
  public static CriterionInfo toInfo(CriterionEntity criterionEntity, String nameCriterionType) {

    if (criterionEntity == null) {
      return new CriterionInfo();
    }

    return CriterionInfo
        .builder()
        .id(criterionEntity.getId())
        .name(criterionEntity.getName())
        .info(criterionEntity.getInfo())
        .nameCriterionType(nameCriterionType)
        .build();
  }

  /**
   * Преобразует модель таблицы "criterion" в объект краткой информации об объекте
   * @param criterionEntity модель таблицы "criterion"
   * @return краткая информация о критерии
   */
  public static ObjectShortInfo toShortInfo(CriterionEntity criterionEntity) {

    if (criterionEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(criterionEntity.getId())
        .nameObject(criterionEntity.getName())
        .build();
  }
}
