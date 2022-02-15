package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.dto.entity.UserEntity;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Реализация сервиса, отвечающего за данные пользователя
 */
@Service
public class UserSecurityServiceFB implements UserSecurityService {

  public static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$");

  /**
   * Проверяет корректность email
   * @param email проверяемый email
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void checkEmailElseThrow(String email) throws InvalidEmailException {

    if (!PATTERN_EMAIL.matcher(email).matches()) {
      throw new InvalidEmailException(String.format(
          "Input value \"%s\" is not an email", email));
    }
  }

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws UserRoleException если модель не имеет роль
   */
  @Override
  public void checkHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String nameTypeObject) throws UserRoleException {

    if (!userEntity.hasRole(nameRole)) {
      throw new UserRoleException(String.format(
          "%s with ID \"%s\" has role \"%s\"", nameTypeObject, userEntity.getId(), nameRole));
    }
  }

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если модель не имеет роль и связано с отсутствием объекта в бд
   */
  @Override
  public void checkHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String nameTypeObject) throws NotFoundException {

    if (!userEntity.hasRole(nameRole)) {
      throw new NotFoundException(String.format(
          "%s with ID \"%s\" not found", nameTypeObject, userEntity.getId()));
    }
  }

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws UserRoleException если модель имеет роль
   */
  @Override
  public void checkNotHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String nameTypeObject) throws UserRoleException {

    if (userEntity.hasRole(nameRole)) {
      throw new UserRoleException(String.format(
          "%s with ID \"%s\" hasn't got role \"%s\"", nameTypeObject, userEntity.getId(), nameRole));
    }
  }

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если модель имеет роль и связано с отсутствием объекта в бд
   */
  @Override
  public void checkNotHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String nameTypeObject) throws NotFoundException {

    if (userEntity.hasRole(nameRole)) {
      throw new NotFoundException(String.format(
          "%s with ID \"%s\" not found", nameTypeObject, userEntity.getId()));
    }
  }
}
