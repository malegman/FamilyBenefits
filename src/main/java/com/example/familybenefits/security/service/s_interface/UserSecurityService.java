package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.exception.*;

/**
 * Интерфейс сервиса, отвечающего за данные пользователя
 */
public interface UserSecurityService {

  /**
   * Проверяет пароль на эквивалентность с повторно введенным и на соответствие политике безопасности паролей.
   * @param password проверяемый пароль
   * @param repeatPassword повторно введенный пароль
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   */
  void checkPasswordElseThrow(String password, String repeatPassword) throws PasswordNotEqualsException, PasswordNotSafetyException;

  /**
   * Проверяет корректность email
   * @param email проверяемый email
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void checkEmailElseThrowInvalidEmail(String email) throws InvalidEmailException;

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws UserRoleException если модель не имеет роль
   */
  void checkHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String nameTypeObject) throws UserRoleException;

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если модель не имеет роль и связано с отсутствием объекта в бд
   */
  void checkHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String nameTypeObject) throws NotFoundException;

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws UserRoleException если модель имеет роль
   */
  void checkNotHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String nameTypeObject) throws UserRoleException;

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если модель имеет роль и связано с отсутствием объекта в бд
   */
  void checkNotHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String nameTypeObject) throws NotFoundException;
}
