package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "institution"
 */
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, String> {

  /**
   * Проверяет наличие учреждения по его названию
   * @param name название учреждения
   * @return true, если учреждение с указанным названием существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие учреждения с отличным от данного ID и данным названием
   * @param id ID учреждения
   * @param name название учреждения
   * @return true, если учреждение с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);
}
