package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

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
}
