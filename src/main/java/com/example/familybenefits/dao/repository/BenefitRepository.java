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
   * Находит пособия, которые есть в указанном городе
   * @param idCity ID города
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM familybenefit.benefits_cities INNER JOIN familybenefit.benefit ON " +
          "familybenefit.benefits_cities.id_benefit = familybenefit.benefit.id " +
          "WHERE familybenefit.benefits_cities.id_city = ?1;")
  Set<BenefitEntity> findAllWhereCityIdEquals(BigInteger idCity);

  /**
   * Находит пособия, которые есть в указанном учреждении
   * @param idInstitution ID учреждения
   * @return множество пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM familybenefit.benefits_institutions INNER JOIN familybenefit.benefit ON " +
          "familybenefit.benefits_institutions.id_benefit = familybenefit.benefit.id " +
          "WHERE familybenefit.benefits_institutions.id_institution = ?1;")
  Set<BenefitEntity> findAllWhereInstitutionIdEquals(BigInteger idInstitution);
}
