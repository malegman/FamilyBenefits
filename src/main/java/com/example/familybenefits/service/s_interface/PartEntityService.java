package com.example.familybenefits.service.s_interface;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса для моделей таблицы, целостность которых зависит от наличия моделей из связанных таблиц
 * @param <T> модель таблицы
 */
public interface PartEntityService<T> {

  /**
   * Проверяет существование модели таблицы по ID
   * @param id ID модели
   * @return true, если модель существует
   */
  boolean existsById(BigInteger id);

  /**
   * Возвращает множество моделей таблицы, в которых есть модели всех связанных таблиц
   * @return множество моделей таблиц
   */
  Set<T> findAllFull();

  /**
   * Возвращает множество моделей таблицы, в которых нет моделей из одной из связанных таблиц
   * @return множество моделей таблиц
   */
  Set<T> findAllPartial();
}
