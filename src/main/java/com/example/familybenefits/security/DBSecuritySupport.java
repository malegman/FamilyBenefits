package com.example.familybenefits.security;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.NotFoundException;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Предоставляет статические универсальные методы для безопасной работы с базой данных
 */
public class DBSecuritySupport {

  /**
   * Проверяет существование в базе данных объекта по его ID
   * @param existFunc функция проверки, принимающая параметр типа {@link String} и возвращающая значение типа {@link Boolean}
   * @param id ID проверяемого объекта
   * @throws NotFoundException если объект не найден
   */
  public static void checkExistenceById(Function<String, Boolean> existFunc, String id) throws NotFoundException {

    if (id == null || !existFunc.apply(preparePostgreSQLString(id))) {
      throw new NotFoundException(String.format(
          "Entity with ID \"%s\" not found in repository %s", id, existFunc.getClass().getName()));
    }
  }

  /**
   * Проверяет существование в базе данных объектов по их ID
   * @param existFunc функция проверки, принимающая параметр типа {@link String} и возвращающая значение типа {@link Boolean}
   * @param idList список ID проверяемых объектов
   * @throws NotFoundException если один из объектов не найден
   */
  public static void checkExistenceById(Function<String, Boolean> existFunc, List<String> idList) throws NotFoundException {

    for (String id : idList) {
      checkExistenceById(existFunc, id);
    }
  }

  /**
   * Проверяет отсутствие в базе данных объекта по его уникальному строковому полю
   * @param existFunc функция проверки, принимающая параметр типа {@link String} и возвращающая значение типа {@link Boolean}
   * @param uniqueStr уникальное строковое поле объекта
   * @throws AlreadyExistsException если объект найден
   */
  public static void checkAbsenceByUniqStr(Function<String, Boolean> existFunc, String uniqueStr) throws AlreadyExistsException {

    if (uniqueStr != null && existFunc.apply(preparePostgreSQLString(uniqueStr))) {
      throw new AlreadyExistsException(String.format(
          "Entity with field \"%s\" already exists in repository %s", uniqueStr, existFunc.getClass().getName()));
    }
  }

  /**
   * Проверяет отсутствие в базе данных объекта с отличным от данного ID с уникальным строковым полем
   * @param existBiFunc функция проверки, принимающая 2 параметра типа {@link String} и возвращающая значение типа {@link Boolean}
   * @param idThis ID данного объекта
   * @param uniqueStr уникальное строковое поле объекта
   * @throws AlreadyExistsException если объект с отличным ID и данным строковым полем найден
   */
  public static void checkAbsenceAnotherByUniqStr(BiFunction<String, String, Boolean> existBiFunc, String idThis, String uniqueStr) throws AlreadyExistsException {

    if (idThis != null && uniqueStr != null && existBiFunc.apply(preparePostgreSQLString(idThis), preparePostgreSQLString(uniqueStr))) {
      throw new AlreadyExistsException(String.format(
          "Entity with field \"%s\" already exists in repository %s", uniqueStr, existBiFunc.getClass().getName()));
    }
  }

  /**
   * Подготавливает строку для вставки в SQL запрос, диалект PostgreSQL
   * @param content проверяемая строка
   * @return обработанная строка
   */
  public static String preparePostgreSQLString(String content) {

    if (content == null) {
      return null;
    }

    return content.replace("'", "''");
  }
}

