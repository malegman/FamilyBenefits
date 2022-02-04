package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.dao.entity.ObjectEntity;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;
import java.util.function.Function;

/**
 * Интерфейс сервиса, отвечающего за целостность базы данных
 */
public interface DBIntegrityService {

  /**
   * Проверяет существование в базе данных объекта по его ID
   * @param existFunc функция проверки
   * @param id ID проверяемого объекта
   * @throws NotFoundException если объект не найден
   */
  void checkExistenceById(Function<String, Boolean> existFunc, String id) throws NotFoundException;

  /**
   * Проверяет существование в базе данных объекта по его ID
   * @param existFunc функция проверки
   * @param entity проверяемый объект
   * @param <E> Тип объекта
   * @throws NotFoundException если объект не найден
   */
  <E extends ObjectEntity> void checkExistenceById(Function<String, Boolean> existFunc, E entity) throws NotFoundException;

  /**
   * Проверяет существование в базе данных объекта из множества по ID
   * @param existFunc функция проверки
   * @param entitySet множество проверяемых объектов
   * @param <E> Тип объекта в множестве
   * @throws NotFoundException если объект из множества не найден
   */
  <E extends ObjectEntity> void checkExistenceById(Function<String, Boolean> existFunc, Set<E> entitySet) throws NotFoundException;

  /**
   * Проверяет отсутствие в базе данных объекта по его уникальному строковому полю
   * @param uniqueStr уникальное строковое поле объекта
   * @throws AlreadyExistsException если объект найден
   */
  void checkAbsenceByUniqStr(Function<String, Boolean> existFunc, String uniqueStr) throws AlreadyExistsException;

  /**
   * Подготавливает строку для вставки в SQL запрос, диалект PostgreSQL
   * @param content проверяемая строка
   * @return обработанная строка
   */
  String preparePostgreSQLString(String content);
}
