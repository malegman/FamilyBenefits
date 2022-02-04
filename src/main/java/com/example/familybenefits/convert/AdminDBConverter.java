package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.dao.entity.RoleEntity;
import com.example.familybenefits.dao.entity.UserEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "user" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 * В рамках объекта "администратор"
 */
public class AdminDBConverter {

  /**
   * Преобразует объект запроса на добавление администратора в модель таблицы "user", обрабатывая строковые поля для БД
   * @param adminAdd объект запроса на добавление администратора
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   */
  static public UserEntity fromAdd(AdminAdd adminAdd, Function<String, String> prepareDBFunc) {

    if (adminAdd == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .name(prepareDBFunc.apply(adminAdd.getName()))
        .email(prepareDBFunc.apply(adminAdd.getEmail()))
        .password(prepareDBFunc.apply(adminAdd.getPassword()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление администратора в модель таблицы "user", обрабатывая строковые поля для БД
   * @param adminUpdate объект запроса на обновление администратора
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   */
  static public UserEntity fromUpdate(AdminUpdate adminUpdate, Function<String, String> prepareDBFunc) {

    if (adminUpdate == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .id(prepareDBFunc.apply(adminUpdate.getId()))
        .name(prepareDBFunc.apply(adminUpdate.getName()))
        .email(prepareDBFunc.apply(adminUpdate.getEmail()))
        .build();
  }

  /**
   * Преобразует модель таблицы "user" в объект информации об администраторе
   * @param userEntity модель таблицы "user"
   * @return информация об администраторе
   */
  static public AdminInfo toInfo(UserEntity userEntity) {

    if (userEntity == null) {
      return new AdminInfo();
    }

    return AdminInfo
        .builder()
        .id(userEntity.getId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .isVerifiedEmail(userEntity.isVerifiedEmail())
        .nameRoleSet(userEntity
                         .getRoleEntitySet()
                         .stream()
                         .map(RoleEntity::getName)
                         .collect(Collectors.toSet()))
        .build();
  }
}
