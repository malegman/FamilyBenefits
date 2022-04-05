package com.example.familybenefits.part_res_rest_api.converters;

import com.example.familybenefits.dto.entities.InstitutionEntity;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionSave;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.security.RandomValue;

import java.util.function.Function;

/**
 * Класс преобразования модели таблицы "institution" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 */
public class InstitutionDBConverter {

  /**
   * Преобразует объект запроса на сохранение учреждения в модель таблицы "institution", обрабатывая строковые поля для БД
   * @param idInstitution ID учреждения. Если {@code null}, значение ID генерируется.
   * @param institutionSave объект запроса на сохранение учреждения
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "institution"
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  public static InstitutionEntity fromSave(String idInstitution, InstitutionSave institutionSave, Function<String, String> prepareDBFunc) throws InvalidStringException {

    if (institutionSave == null) {
      return new InstitutionEntity();
    }

    return InstitutionEntity
        .builder()
        .id(idInstitution != null
                ? idInstitution
                : RandomValue.randomString(R.ID_LENGTH))
        .name(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getName(), "name", true)))
        .address(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getAddress(), "address", true)))
        .info(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getInfo(), "info", true)))
        .phone(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getPhone(), "phone", true)))
        .email(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getEmail(), "email", false)))
        .schedule(prepareDBFunc.apply(FieldConverter.withSymbolsField(institutionSave.getSchedule(), "schedule", true)))
        .idCity(institutionSave.getIdCity())
        .build();
  }

  /**
   * Преобразует модель таблицы "institution" в объект информации об учреждении
   * @param institutionEntity модель таблицы "institution"
   * @param nameCity название города учреждения
   * @return информация об учреждении
   */
  public static InstitutionInfo toInfo(InstitutionEntity institutionEntity, String nameCity) {

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
        .nameCity(nameCity)
        .build();
  }

  /**
   * Преобразует модель таблицы "institution" в объект краткой информации об объекте
   * @param institutionEntity модель таблицы "institution"
   * @return краткая информация об учреждении
   */
  public static ObjectShortInfo toShortInfo(InstitutionEntity institutionEntity) {

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
