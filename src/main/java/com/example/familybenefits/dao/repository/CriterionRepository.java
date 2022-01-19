package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "criterion"
 */
public interface CriterionRepository extends JpaRepository<CriterionEntity, BigInteger> {

  /**
   * Находит критерии с указанным типом критерия
   * @param criterionTypeEntity тип критерия
   * @return множество критериев
   */
  Set<CriterionEntity> findAllByCriterionType(CriterionTypeEntity criterionTypeEntity);

  /**
   * Проверяет наличие критерия по его названию
   * @param name название критерия
   * @return true, если критерий с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Находит критерии с указанным типом критерия
   * @return множество критериев
   */
  Set<CriterionEntity> findAllByCriterionTypeExists();
}
