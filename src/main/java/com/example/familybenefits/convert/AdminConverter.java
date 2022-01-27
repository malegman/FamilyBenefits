package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.dao.entity.RoleEntity;
import com.example.familybenefits.dao.entity.UserEntity;

import java.util.stream.Collectors;

/**
 * Класс преобразования модели таблицы "user" в другие объекты и получения из других объектов.
 * В рамках объекта "администратор"
 */
public class AdminConverter {

  /**
   * Преобразует объект запроса на добавление администратора в модель таблицы "user"
   * @param adminAdd объект запроса на добавление администратора
   * @return модель таблицы "user"
   */
  static public UserEntity fromAdd(AdminAdd adminAdd) {

    if (adminAdd == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .name(adminAdd.getName())
        .email(adminAdd.getEmail())
        .password(adminAdd.getPassword())
        .build();
  }

  /**
   * Преобразует объект запроса на обновление администратора в модель таблицы "user"
   * @param adminUpdate объект запроса на обновление администратора
   * @return модель таблицы "user"
   */
  static public UserEntity fromUpdate(AdminUpdate adminUpdate) {

    if (adminUpdate == null) {
      return new UserEntity();
    }

    return UserEntity
        .builder()
        .id(adminUpdate.getId())
        .name(adminUpdate.getName())
        .email(adminUpdate.getEmail())
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
