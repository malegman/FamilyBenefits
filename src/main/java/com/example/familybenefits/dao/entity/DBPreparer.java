package com.example.familybenefits.dao.entity;

import java.util.function.Function;

/**
 * Предоставляет функцию для обработки объекта перед записью в базу данных
 */
public interface DBPreparer {

  /**
   * Обработывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  DBPreparer prepareForDB(Function<String, String> prepareFunc);
}
