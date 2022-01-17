package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Set;

/**
 * Репозиторий, работающий с моделью таблицы "benefit"
 */
public interface BenefitRepository extends JpaRepository<BenefitEntity, BigInteger> {

  /**
   * Находит пособия, которые есть в указанных городах
   * @param cityEntitySet множество городов
   * @return множество пособий
   */
  Set<BenefitEntity> findAllByCityEntitySet(Set<CityEntity> cityEntitySet);
}
