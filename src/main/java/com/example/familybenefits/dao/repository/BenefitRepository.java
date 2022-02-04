package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.BenefitEntity;
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
}
