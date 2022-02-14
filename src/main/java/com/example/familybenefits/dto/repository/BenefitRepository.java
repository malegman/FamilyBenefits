package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "benefit"
 */
public interface BenefitRepository extends JpaRepository<BenefitEntity, String> {

  /**
   * Проверяет наличие пособия по его названию
   * @param name название пособия
   * @return true, если пособие с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие пособия с отличным от данного ID и данным названием
   * @param id ID пособия
   * @param name название пособия
   * @return true, если пособие с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);
}
