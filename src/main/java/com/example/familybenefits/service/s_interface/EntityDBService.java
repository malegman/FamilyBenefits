package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.dao.entity.ObjectEntity;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.function.Function;

/**
 * Интерфейс сервиса модели таблицы, целостность которой зависит от наличия моделей из связанных таблиц
 * @param <E> модель таблицы
 * @param <R> тип репозитория
 */
public interface EntityDBService<E extends ObjectEntity, R extends JpaRepository<E, String>> {

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  R getRepository();

  /**
   * Возвращает множество моделей таблицы, в которых есть модели всех связанных таблиц
   * @return множество моделей таблиц
   */
  Set<E> findAllFull();

  /**
   * Возвращает множество моделей таблицы, в которых нет моделей из одной из связанных таблиц
   * @return множество моделей таблиц
   */
  Set<E> findAllPartial();
}
