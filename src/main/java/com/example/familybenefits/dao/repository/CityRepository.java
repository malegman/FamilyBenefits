package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "city"
 */
public interface CityRepository extends JpaRepository<CityEntity, String> {

  /**
   * Проверяет наличие города по его названию
   * @param name название города
   * @return true, если город с указанным именем существует
   */
  boolean existsByName(String name);
}
