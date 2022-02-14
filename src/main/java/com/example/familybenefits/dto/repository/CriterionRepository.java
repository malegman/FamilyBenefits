package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.CriterionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "criterion"
 */
public interface CriterionRepository extends JpaRepository<CriterionEntity, String> {

  /**
   * Проверяет наличие критерия по его названию
   * @param name название критерия
   * @return true, если критерий с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие критерия с отличным от данного ID и данным названием
   * @param id ID критерия
   * @param name название критерия
   * @return true, если критерий с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);
}
