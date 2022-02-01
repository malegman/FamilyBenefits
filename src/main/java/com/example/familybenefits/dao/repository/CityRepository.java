package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

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
}
