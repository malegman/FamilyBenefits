package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserUpdate;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.RoleEntity;
import com.example.familybenefits.dao.entity.UserEntity;

import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "user" в другие объекты и получения из других объектов.
 * В рамках объекта "пользователь"
 */
public class UserConverter {

  /**
   * Преобразует объект запроса на добавление пользователя в модель таблицы "user".
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userAdd объект запроса на добавление пользователя
   * @return модель таблицы "user"
   */
  static public UserEntity fromAdd(UserAdd userAdd) {

    if (userAdd == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .name(userAdd.getName())
        .email(userAdd.getEmail())
        .password(userAdd.getPassword())
        .cityEntity(new CityEntity(userAdd.getIdCity()))
        .criterionEntitySet(userAdd.getIdCriterionSet()
                                .stream()
                                .map(CriterionEntity::new)
                                .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует объект запроса на обновление пользователя в модель таблицы "user".
   * В преобразовании не участвуют поля с датами рождениями детей и датой рождения пользователя
   * @param userUpdate объект запроса на обновление пользователя
   * @return модель таблицы "user"
   */
  static public UserEntity fromUpdate(UserUpdate userUpdate) {

    if (userUpdate == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .id(userUpdate.getId())
        .name(userUpdate.getName())
        .email(userUpdate.getEmail())
        .cityEntity(new CityEntity(userUpdate.getIdCity()))
        .criterionEntitySet(userUpdate.getIdCriterionSet()
                                .stream()
                                .map(CriterionEntity::new)
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
        .shortCity(CityConverter.toShortInfo(userEntity.getCityEntity()))
        .criterionSet(userEntity.getCriterionEntitySet()
                          .stream()
                          .map(CriterionConverter::toInfo)
                          .collect(Collectors.toSet()))
        .build();
  }
}
