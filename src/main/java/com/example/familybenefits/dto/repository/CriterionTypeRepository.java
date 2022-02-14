package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "criterion_type"
 */
public interface CriterionTypeRepository extends JpaRepository<CriterionTypeEntity, String> {

  /**
   * Проверяет наличие типа критерия по его названию
   * @param name название типа критерия
   * @return true, если тип критерия с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие типа критерия с отличным от данного ID и данным названием
   * @param id ID типа критерия
   * @param name название типа критерия
   * @return true, если тип критерия с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);
}
