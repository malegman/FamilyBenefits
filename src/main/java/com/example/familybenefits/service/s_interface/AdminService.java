package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.exception.*;

/**
 * Интерфейс сервиса, управляющего объектом "администратор"
 */
public interface AdminService {

  /**
   * Добавляет администратора по запросу на добавление
   * @param adminAdd объект запроса на добавление администратора
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   * @throws NotEqualException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void add(AdminAdd adminAdd) throws AlreadyExistsException, PasswordNotSafetyException, NotEqualException, InvalidEmailException;

  /**
   * Обновляет администратора по запросу на обновление
   * @param adminUpdate объект запроса на обновление администратора
   * @throws NotFoundException если администратор с указанными данными не найден
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void update(AdminUpdate adminUpdate) throws NotFoundException, InvalidEmailException;

  /**
   * Удаляет администратора по его ID или удаляет роль "ROLE_ADMIN" у пользователя
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с указанным ID не найден
   * @throws UserRoleException если администратор имеет роль "ROLE_SUPER_ADMIN"
   */
  void delete(String idAdmin) throws NotFoundException, UserRoleException;

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с данным ID не найден
   */
  AdminInfo read(String idAdmin) throws NotFoundException;

  /**
   * Добавляет роль "ROLE_ADMIN" пользователю
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с данным ID не найден
   * @throws UserRoleException если пользователь имеет роль "ROLE_ADMIN" или не имеет роль "ROLE_USER"
   */
  void fromUser(String idUser) throws NotFoundException, UserRoleException;

  /**
   * Добавляет роль "ROLE_USER" администратору
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws UserRoleException если пользователь имеет роль "ROLE_USER" или не имеет роль "ROLE_ADMIN"
   */
  void toUser(String idAdmin) throws NotFoundException, UserRoleException;

  /**
   * Передает роль "ROLE_SUPER_ADMIN" указанному администратору, удаляя данную роль у текущего администратора
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws UserRoleException если администратор имеет роль "ROLE_SUPER_ADMIN"
   */
  void toSuper(String idAdmin) throws NotFoundException, UserRoleException;
}
