package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserUpdate;
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
   * Преобразует объект запроса на добавление пользователя в модель таблицы "user", обрабатывая строковые поля для БД.
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userAdd объект запроса на добавление пользователя
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   */
  static public UserEntity fromAdd(UserAdd userAdd, Function<String, String> prepareDBFunc) {

    if (userAdd == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .name(prepareDBFunc.apply(userAdd.getName()))
        .email(prepareDBFunc.apply(userAdd.getEmail()))
        .password(prepareDBFunc.apply(userAdd.getPassword()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(userAdd.getIdCity())))
        .criterionEntitySet(userAdd.getIdCriterionSet()
                                .stream()
                                .map(id -> new CriterionEntity(prepareDBFunc.apply(id)))
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление пользователя в модель таблицы "user", обрабатывая строковые поля для БД.
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userUpdate объект запроса на обновление пользователя
   * @param prepareDBFunc функция обработки строки для БД
   * @return модель таблицы "user"
   */
  static public UserEntity fromUpdate(UserUpdate userUpdate, Function<String, String> prepareDBFunc) {

    if (userUpdate == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .id(prepareDBFunc.apply(userUpdate.getId()))
        .name(prepareDBFunc.apply(userUpdate.getName()))
        .email(prepareDBFunc.apply(userUpdate.getEmail()))
        .cityEntity(new CityEntity(prepareDBFunc.apply(userUpdate.getIdCity())))
        .criterionEntitySet(userUpdate.getIdCriterionSet()
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

    if (userEntity == null) {
      return new UserInfo();
    }

    return UserInfo
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
        .shortCity(CityDBConverter.toShortInfo(userEntity.getCityEntity()))
        .criterionSet(userEntity.getCriterionEntitySet()
                          .stream()
                          .map(CriterionDBConverter::toInfo)
                          .collect(Collectors.toSet()))
        .build();
  }
}
