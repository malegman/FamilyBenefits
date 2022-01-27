package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "institution"
 */
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, BigInteger> {

  /**
   * Проверяет наличие учреждения по его названию
   * @param name название учреждения
   * @return true, если учреждение с указанным названием существует
   */
  boolean existsByName(String name);

  /**
   * Находит учреждения, которые есть в указанном городе
   * @param cityEntity город
   * @return множество учреждений
   */
  Set<InstitutionEntity> findAllByCityEntity(CityEntity cityEntity);

  /**
   * Находит учреждения, которые есть в указанном пособии
   * @param idBenefit ID пособия
   * @return множество учреждений
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM family_benefit.benefits_institutions INNER JOIN family_benefit.institution ON " +
          "family_benefit.benefits_institutions.id_institution = family_benefit.institution.id " +
          "WHERE family_benefit.benefits_institutions.id_benefit = ?1;")
  Set<InstitutionEntity> findAllWhereBenefitIdEquals(BigInteger idBenefit);
}
