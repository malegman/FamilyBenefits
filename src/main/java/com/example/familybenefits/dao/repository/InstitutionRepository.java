package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
