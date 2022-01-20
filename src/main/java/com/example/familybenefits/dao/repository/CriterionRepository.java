package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "criterion"
 */
public interface CriterionRepository extends JpaRepository<CriterionEntity, BigInteger> {

  /**
   * Находит критерии с указанным типом критерия
   * @param criterionTypeEntity тип критерия
   * @return множество критерий
   */
  Set<CriterionEntity> findAllByCriterionType(CriterionTypeEntity criterionTypeEntity);

  /**
   * Проверяет наличие критерия по его названию
   * @param name название критерия
   * @return true, если критерий с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Находит критерии с существующим типом критерия
   * @return множество критерий
   */
  Set<CriterionEntity> findAllByCriterionTypeExists();

  /**
   * Находит критерии с без типа критерия
   * @return множество критерий
   */
  Set<CriterionEntity> findAllByCriterionTypeIsNull();

  /**
   * Находит учреждения, которые есть в указанном пособии
   * @param idBenefit ID пособия
   * @return множество учреждений
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM familybenefit.criterion " +
          "INNER JOIN familybenefit.benefits_criteria ON familybenefit.benefits_criteria.id_criterion = familybenefit.criterion.id " +
          "WHERE familybenefit.benefits_criteria.id_benefit = ?1 " +
          "AND familybenefit.criterion.id_type IS NOT NULL;")
  Set<CriterionEntity> findAllFullWhereBenefitIdEquals(BigInteger idBenefit);
}
