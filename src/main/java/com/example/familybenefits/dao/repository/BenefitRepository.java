package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "benefit"
 */
public interface BenefitRepository extends JpaRepository<BenefitEntity, BigInteger> {

  /**
   * Проверяет наличие пособия по его названию
   * @param name название пособия
   * @return true, если пособие с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Находит полные пособия
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city IS NOT NULL " +
          "AND family_benefit.benefits_criteria.id_criterion IS NOT NULL " +
          "AND family_benefit.benefits_institutions.id_institution IS NOT NULL;")
  Set<BenefitEntity> findAllFull();

  /**
   * Находит неполные пособия
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city IS NULL " +
          "OR family_benefit.benefits_criteria.id_criterion IS NULL " +
          "OR family_benefit.benefits_institutions.id_institution IS NULL;")
  Set<BenefitEntity> findAllPartial();

  /**
   * Находит полные пособия, которые есть в указанном городе
   * @param idCity ID города
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city = ?1 " +
          "AND family_benefit.benefits_criteria.id_criterion IS NOT NULL " +
          "AND family_benefit.benefits_institutions.id_institution IS NOT NULL;")
  Set<BenefitEntity> findAllFullWhereCityIdEquals(BigInteger idCity);

  /**
   * Находит полные пособия, которые есть в указанном учреждении
   * @param idInstitution ID учреждения
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city IS NOT NULL " +
          "AND family_benefit.benefits_criteria.id_criterion IS NOT NULL " +
          "AND family_benefit.benefits_institutions.id_institution = ?1;")
  Set<BenefitEntity> findAllFullWhereInstitutionIdEquals(BigInteger idInstitution);
}
