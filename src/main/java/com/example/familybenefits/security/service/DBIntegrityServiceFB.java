package com.example.familybenefits.security.service;

import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.function.Function;

/**
 * Реализация сервиса, отвечающего за целостность базы данных
 */
@Service
public class DBIntegrityServiceFB implements DBIntegrityService {

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObject ID проверяемого объекта
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если объект с указанным ID не найден
   */
  @Override
  public void checkExistenceByIdElseThrow(Function<BigInteger, Boolean> existFunc, BigInteger idObject, String messagePattern) throws NotFoundException {

    if (!existFunc.apply(idObject)) {
      throw new NotFoundException(String.format(messagePattern, idObject));
    }
  }

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObjectSet множество ID проверяемых объектов
   * @param messagePattern шаблон сообщения об ошибке
   * @throws NotFoundException если объект с указанным ID не найден
   */
  @Override
  public void checkExistenceByIdElseThrow(Function<BigInteger, Boolean> existFunc, Set<BigInteger> idObjectSet, String messagePattern) throws NotFoundException {

    for (BigInteger idObject : idObjectSet) {
      checkExistenceByIdElseThrow(existFunc, idObject, messagePattern);
    }
  }

  /**
   * Проверяет отсутствие объекта по его уникальному строковому параметру
   * @param existFunc функция, выполняющая проверку
   * @param uniqStrObject уникальный строковый параметр объекта
   * @param messagePattern шаблон сообщения об ошибке
   * @throws AlreadyExistsException если объект с указанным строковым параметром существует
   */
  @Override
  public void checkAbsenceByUniqStrElseThrow(Function<String, Boolean> existFunc, String uniqStrObject, String messagePattern) throws AlreadyExistsException {

    if (existFunc.apply(uniqStrObject)) {
      throw new AlreadyExistsException(String.format(messagePattern, uniqStrObject));
    }
  }

  /**
   * Подготавливает строку для вставки в SQL запрос, диалект PostgreSQL
   * @param content проверяемая строка
   * @return обработанная строка
   */
  @Override
  public String preparePostgreSQLString(String content) {

    if (content == null) {
      return null;
    }

    return content.replace("'", "''");
  }
}
