package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

  /**
   * Проверяет наличие города с отличным от данного ID и данным названием
   * @param id ID города
   * @param name название города
   * @return true, если город с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);

  /**
   * Возвращает список городов по фильтру: название города, ID пособия, ID учреждения.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название города
   * @param idBenefit ID пособия
   * @param idInstitution ID учреждения
   * @return список городов, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT DISTINCT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.city " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_city = family_benefit.city.id " +
          "INNER JOIN family_benefit.institution ON family_benefit.institution.id_city = family_benefit.city.id " +
          "WHERE (?1 = '' OR family_benefit.city.name = ?1) " +
          "AND (?2 = '' OR family_benefit.benefits_cities.id_benefit = ?2) " +
          "AND (?3 = '' OR family_benefit.institution.id = ?3);")
  List<CityEntity> findAllFilter(String name, String idBenefit, String idInstitution);

  /**
   * Возвращает список неполных городов: без пособия или без учреждения
   * @return список городов
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.city " +
          "LEFT JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_city = family_benefit.city.id " +
          "LEFT JOIN family_benefit.institution ON family_benefit.institution.id_city = family_benefit.city.id " +
          "WHERE family_benefit.benefits_cities.id_benefit = '' OR family_benefit.institution.id = '';")
  List<CityEntity> findAllPartial();
}
