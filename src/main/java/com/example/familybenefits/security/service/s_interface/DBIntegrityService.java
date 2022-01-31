package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;
import java.util.function.Function;

/**
 * Интерфейс сервиса, отвечающего за целостность базы данных
 */
public interface DBIntegrityService {

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObject ID проверяемого объекта
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если объект с указанным ID не найден
   */
  void checkExistenceByIdElseThrowNotFound(Function<BigInteger, Boolean> existFunc, BigInteger idObject, String messagePattern) throws NotFoundException;

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObjectSet множество ID проверяемых объектов
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если объект с указанным ID не найден
   */
  void checkExistenceByIdElseThrowNotFound(Function<BigInteger, Boolean> existFunc, Set<BigInteger> idObjectSet, String messagePattern) throws NotFoundException;

  /**
   * Проверяет отсутствие объекта по его уникальному строковому параметру
   * @param existFunc функция, выполняющая проверку
   * @param uniqStrObject уникальный строковый параметр объекта
   * @param messagePattern шаблон сообщения об ошибке
   * @throws AlreadyExistsException если объект с указанным строковым параметром существует
   */
  void checkAbsenceByUniqStrElseThrowAlreadyExists(Function<String, Boolean> existFunc, String uniqStrObject, String messagePattern) throws AlreadyExistsException;

  /**
   * Подготавливает строку для вставки в SQL запрос, диалект PostgreSQL
   * @param content проверяемая строка
   * @return обработанная строка
   */
  String preparePostgreSQLString(String content);
}
