package com.example.familybenefits.part_res_rest_api.converters;

import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.admin.AdminSave;
import com.example.familybenefits.part_res_rest_api.api_model.admin.AdminInfo;
import com.example.familybenefits.dto.entities.RoleEntity;
import com.example.familybenefits.dto.entities.UserEntity;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.security.RandomValue;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "user" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 * В рамках объекта "администратор"
 */
public class AdminDBConverter {

  /**
   * Преобразует объект запроса на сохранение администратора в модель таблицы "user", обрабатывая строковые поля для БД
   * @param idAdmin ID администратора. Если {@code null}, значение ID генерируется.
   * @param adminSave объект запроса на сохранение администратора
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  public static UserEntity fromSave(String idAdmin, AdminSave adminSave, Function<String, String> prepareDBFunc) throws InvalidStringException {

    if (adminSave == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .id(idAdmin != null
                ? idAdmin
                : RandomValue.randomString(R.ID_LENGTH))
        .name(prepareDBFunc.apply(FieldConverter.withSymbolsField(adminSave.getName(), "name", true)))
        .email(prepareDBFunc.apply(FieldConverter.withSymbolsField(adminSave.getEmail(), "email", true)))
        .build();
  }

  /**
   * Преобразует модель таблицы "user" в объект информации об администраторе
   * @param userEntity модель таблицы "user"
   * @param roleEntityList список моделей таблицы "role", связанных с администратором
   * @return информация об администраторе
   */
  public static AdminInfo toInfo(UserEntity userEntity, List<RoleEntity> roleEntityList) {

    if (userEntity == null) {
      return new AdminInfo();
    }

    return AdminInfo
        .builder()
        .id(userEntity.getId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .nameRoleList(roleEntityList
                          .stream()
                          .map(RoleEntity::getName)
                          .collect(Collectors.toList()))
        .build();
  }
}
