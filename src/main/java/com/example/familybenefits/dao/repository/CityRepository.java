package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "city"
 */
public interface CityRepository extends JpaRepository<CityEntity, BigInteger> {

  /**
   * Проверяет наличие города по его названию
   * @param name название города
   * @return true, если город с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Находит города, которые есть в указанном пособии
   * @param idBenefit ID пособия
   * @return множество городов
   */
  @Query(nativeQuery = true,
      value = "SELECT *" +
          "FROM familybenefit.benefits_cities INNER JOIN familybenefit.city ON " +
          "familybenefit.benefits_cities.id_city = familybenefit.city.id " +
          "WHERE familybenefit.benefits_cities.id_benefit = ?1;")
  Set<CityEntity> findAllWhereBenefitIdEquals(BigInteger idBenefit);
}
