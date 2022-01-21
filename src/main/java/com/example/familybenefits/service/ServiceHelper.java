package com.example.familybenefits.service;

import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * Класс, предоставляющий вспомогательные методы для сервисов
 */
public class ServiceHelper {

  /**
   * Проверяет существование объекта по его ID
   * @param existFunc функция, выполняющая проверку
   * @param idObject ID проверяемого объекта
   * @param patternErrorMsg шаблон сообщения об ошибке
   * @throws NotFoundException если объект с указанным ID не найден
   */
  public static void checkExistenceObjectByIdElseThrow(Function<BigInteger, Boolean> existFunc, BigInteger idObject, String patternErrorMsg) throws NotFoundException {

    if (!existFunc.apply(idObject)) {
      throw new NotFoundException(String.format(
          patternErrorMsg, idObject
      ));
    }
  }

  /**
   * Проверяет отсутствие объекта по его уникальному строковому параметру
   * @param existFunc функция, выполняющая проверку
   * @param uniqStrObject уникальный строковый параметр объекта
   * @param patternErrorMsg шаблон сообщения об ошибке
   * @throws AlreadyExistsException если объект с указанным строковым параметром существует
   */
  public static void checkAbsenceObjectByUniqStrElseThrow(Function<String, Boolean> existFunc, String uniqStrObject, String patternErrorMsg) throws AlreadyExistsException {

    if (existFunc.apply(uniqStrObject)) {
      throw new AlreadyExistsException(String.format(
          patternErrorMsg, uniqStrObject
      ));
    }
  }
}
