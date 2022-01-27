package com.example.familybenefits.service;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.exception.*;

import java.math.BigInteger;

/**
 * Интерфейс сервиса, управляющего объектом "администратор"
 */
public interface AdminService {

  /**
   * Добавляет администратора по запросу на добавление
   * @param adminAdd adminAdd объект запроса на добавление администратора
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соотвествует политике безопасности
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void add(AdminAdd adminAdd) throws AlreadyExistsException, PasswordNotSafetyException, PasswordNotEqualsException, InvalidEmailException;

  /**
   * Обновляет администратора по запросу на обновление
   * @param adminUpdate объект запроса на обновление администратора
   * @throws NotFoundException если администратор с указанными данными не найден
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void update(AdminUpdate adminUpdate) throws NotFoundException, InvalidEmailException;

  /**
   * Удаляет администратора по его ID или удаляет роль администратора у пользователя
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с указанным ID не найден
   */
  void delete(BigInteger idAdmin) throws NotFoundException;

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с указанным ID не найдено
   */
  AdminInfo read(BigInteger idAdmin) throws NotFoundException;

  /**
   * Добавляет роль администратора пользователю
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с данным ID не найден
   * @throws AlreadyExistsException если пользователь имеет роль администратора
   */
  void fromUser(BigInteger idUser) throws NotFoundException, AlreadyExistsException;

  /**
   * Добавляет роль пользователя администратору
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws AlreadyExistsException если администратор имеет роль пользователя
   */
  void toUser(BigInteger idAdmin) throws NotFoundException, AlreadyExistsException;
}
