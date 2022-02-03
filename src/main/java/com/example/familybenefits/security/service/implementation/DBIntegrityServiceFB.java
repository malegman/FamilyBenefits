package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
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
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если объект с указанным ID не найден
   */
  @Override
  public void checkExistenceByIdElseThrowNotFound(Function<BigInteger, Boolean> existFunc, BigInteger idObject, String nameTypeObject) throws NotFoundException {

    if (!existFunc.apply(idObject)) {
      throw new NotFoundException(String.format(
          "%s with ID \"%s\" not found", nameTypeObject, idObject));
    }
  }

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObjectSet множество ID проверяемых объектов
   * @param nameTypeObject название проверяемого объекта
   * @throws NotFoundException если объект с указанным ID не найден
   */
  @Override
  public void checkExistenceByIdElseThrowNotFound(Function<BigInteger, Boolean> existFunc, Set<BigInteger> idObjectSet, String nameTypeObject) throws NotFoundException {

    for (BigInteger idObject : idObjectSet) {
      checkExistenceByIdElseThrowNotFound(existFunc, idObject, nameTypeObject);
    }
  }

  /**
   * Проверяет отсутствие объекта по его уникальному строковому параметру
   * @param existFunc функция, выполняющая проверку
   * @param uniqStrObject уникальный строковый параметр объекта
   * @param nameTypeObject название проверяемого объекта
   * @throws AlreadyExistsException если объект с указанным строковым параметром существует
   */
  @Override
  public void checkAbsenceByUniqStrElseThrowAlreadyExists(Function<String, Boolean> existFunc, String uniqStrObject, String nameTypeObject) throws AlreadyExistsException {

    if (existFunc.apply(uniqStrObject)) {
      throw new AlreadyExistsException(String.format(
          "%s with unique field \"%s\" already exists", nameTypeObject, uniqStrObject));
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
