package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.user.UserSave;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.RoleEntity;
import com.example.familybenefits.dao.entity.UserEntity;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "user" в другие объекты и получения из других объектов, обрабатывая строковые поля для БД.
 * В рамках объекта "пользователь"
 */
public class UserDBConverter {

  /**
   * Преобразует объект запроса на сохранение пользователя в модель таблицы "user", обрабатывая строковые поля для БД.
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userSave объект запроса на сохранение пользователя
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   */
  static public UserEntity fromSave(UserSave userSave, Function<String, String> prepareDBFunc) {

    if (userSave == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .name(prepareDBFunc.apply(userSave.getName()))
        .email(prepareDBFunc.apply(userSave.getEmail()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(userSave.getIdCity())))
        .criterionEntitySet(userSave.getIdCriterionSet()
                                .stream()
                                .map(id -> new CriterionEntity(prepareDBFunc.apply(id)))
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует модель таблицы "user" в объект информации о пользователе.
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userEntity модель таблицы "user"
   * @return информация о пользователе
   */
  static public UserInfo toInfo(UserEntity userEntity) {

    if (userEntity == null || userEntity.getCityEntity() == null) {
      return new UserInfo();
    }

    return UserInfo
        .builder()
        .id(userEntity.getId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .nameRoleSet(userEntity
                         .getRoleEntitySet()
                         .stream()
                         .map(RoleEntity::getName)
                         .collect(Collectors.toSet()))
        .nameCity(userEntity.getCityEntity().getName())
        .build();
  }
}
