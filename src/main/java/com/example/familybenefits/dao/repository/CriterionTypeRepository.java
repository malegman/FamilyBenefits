package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

/**
 * Репозиторий, работающий с моделью таблицы "criterion_type"
 */
public interface CriterionTypeRepository extends JpaRepository<CriterionTypeEntity, BigInteger> {

  /**
   * Проверяет наличие типа критерия по его названию
   * @param name название типа критерия
   * @return true, если тип критерия с указанным именем существует
   */
  boolean existsByName(String name);
}
