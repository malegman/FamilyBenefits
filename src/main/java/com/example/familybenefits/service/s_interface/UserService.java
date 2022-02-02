package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserUpdate;
import com.example.familybenefits.exception.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "пользователь"
 */
public interface UserService {

  /**
   * Добавляет нового пользователя. Регистрация гостя
   * @param userAdd объект запроса на добавление пользователя
   * @throws NotFoundException если город или критерии с указанными данными не найдены
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  void add(UserAdd userAdd) throws
      NotFoundException,
      AlreadyExistsException,
      PasswordNotSafetyException,
      PasswordNotEqualsException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException;

  /**
   * Обновляет пользователя по запросу на обновление
   * @param userUpdate объект запроса на обновление пользователя
   * @throws NotFoundException если пользователь, город или критерии с указанными данными не найдены
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  void update(UserUpdate userUpdate) throws
      NotFoundException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException;

  /**
   * Удаляет пользователя по его ID или удаляет роль "ROLE_USER" у администратора
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  void delete(BigInteger idUser) throws NotFoundException;

  /**
   * Возвращает пользователя об учреждении по его ID
   * @param idUser ID пользователя
   * @return информация о пользователе
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  UserInfo read(BigInteger idUser) throws NotFoundException;

  /**
   * Возвращает множество подобранных пособий для пользователя
   * @param idUser ID пользователя
   * @return множество подобранных пособий
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  Set<BenefitInfo> getBenefits(BigInteger idUser) throws NotFoundException, DateTimeException;

  /**
   * Возвращает дополнительные данные для пользователя.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях
   * @return дополнительные данные для пользователя
   */
  UserInitData getInitData();
}
