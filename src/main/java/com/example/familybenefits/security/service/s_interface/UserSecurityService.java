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
   * @param notEqMessage сообщение об ошибке о не эквивалентных паролях
   * @param notSfMessage сообщение об ошибке о не безопасном пароле
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   */
  void checkPasswordElseThrow(String password, String repeatPassword, String notEqMessage, String notSfMessage) throws PasswordNotEqualsException, PasswordNotSafetyException;

  /**
   * Проверяет корректность email
   * @param email проверяемый email
   * @param message сообщение об ошибке
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void checkEmailElseThrowInvalidEmail(String email, String message) throws InvalidEmailException;

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param messagePattern шаблон сообщения об ошибке
   * @throws UserRoleException если модель не имеет роль
   */
  void checkHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String messagePattern) throws UserRoleException;

  /**
   * Проверяет наличие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если модель не имеет роль и связано с отсутствием объекта в бд
   */
  void checkHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String messagePattern) throws NotFoundException;

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param messagePattern шаблон сообщения об ошибке
   * @throws UserRoleException если модель имеет роль
   */
  void checkNotHasRoleElseThrowUserRole(UserEntity userEntity, String nameRole, String messagePattern) throws UserRoleException;

  /**
   * Проверяет отсутствие указанной роли по её названию у указанной модели таблицы "user"
   * @param userEntity модель таблицы "user", роль которой необходимо проверить
   * @param nameRole название проверяемой роли
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если модель имеет роль и связано с отсутствием объекта в бд
   */
  void checkNotHasRoleElseThrowNotFound(UserEntity userEntity, String nameRole, String messagePattern) throws NotFoundException;
}
