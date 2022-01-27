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
  Set<CriterionEntity> findAllByCriterionTypeIsNotNull();

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
          "FROM family_benefit.criterion " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_criterion = family_benefit.criterion.id " +
          "WHERE family_benefit.benefits_criteria.id_benefit = ?1 " +
          "AND family_benefit.criterion.id_type IS NOT NULL;")
  Set<CriterionEntity> findAllFullWhereBenefitIdEquals(BigInteger idBenefit);
}
