package com.example.familybenefits.security.service.s_interface;

import java.util.function.Function;

/**
 * Предоставляет функцию для обработки объекта перед записью в базу данных
 */
public interface EntityPreparer {

  /**
   * Обрабатывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  EntityPreparer prepareForDB(Function<String, String> prepareFunc);
}
