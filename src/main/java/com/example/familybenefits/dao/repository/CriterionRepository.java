package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CriterionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

/**
 * Репозиторий, работающий с моделью таблицы "criterion"
 */
public interface CriterionRepository extends JpaRepository<CriterionEntity, BigInteger> {

  /**
   * Проверяет наличие критерия по его названию
   * @param name название критерия
   * @return true, если критерий с указанным именем существует
   */
  boolean existsByName(String name);
}
