package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.dao.entity.ObjectEntity;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;

/**
 * Реализация сервиса, отвечающего за целостность базы данных
 */
@Service
public class DBIntegrityServiceFB implements DBIntegrityService {

  /**
   * Проверяет существование в базе данных объекта по его ID
   * @param existFunc функция проверки
   * @param id ID проверяемого объекта
   * @throws NotFoundException если объект не найден
   */
  @Override
  public void checkExistenceById(Function<String, Boolean> existFunc, String id) throws NotFoundException {

    if (!existFunc.apply(id)) {
      throw new NotFoundException(String.format(
          "Entity with ID \"%s\" not found in repository %s", id, existFunc.getClass().getName()));
    }
  }

  /**
   * Проверяет существование в базе данных объекта по его ID
   * @param existFunc функция проверки
   * @param entity проверяемый объект
   * @param <E> Тип объекта
   * @throws NotFoundException если объект не найден
   */
  @Override
  public <E extends ObjectEntity> void checkExistenceById(Function<String, Boolean> existFunc, E entity) throws NotFoundException {

    checkExistenceById(existFunc, entity.getId());
  }

  /**
   * Проверяет существование в базе данных объекта из множества по ID
   * @param existFunc функция проверки
   * @param entitySet множество проверяемых объектов
   * @param <E> Тип объекта в множестве
   * @throws NotFoundException если объект из множества не найден
   */
  @Override
  public <E extends ObjectEntity> void checkExistenceById(Function<String, Boolean> existFunc, Set<E> entitySet) throws NotFoundException {

    for (E entity : entitySet) {
      checkExistenceById(existFunc, entity.getId());
    }
  }

  /**
   * Проверяет отсутствие в базе данных объекта по его уникальному строковому полю
   * @param uniqueStr уникальное строковое поле объекта
   * @throws AlreadyExistsException если объект найден
   */
  @Override
  public void checkAbsenceByUniqStr(Function<String, Boolean> existFunc, String uniqueStr) throws AlreadyExistsException {

    if (existFunc.apply(uniqueStr)) {
      throw new AlreadyExistsException(String.format(
          "Benefit with name \"%s\" already exists", uniqueStr));
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
