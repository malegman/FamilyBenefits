package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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
   * Возвращает город пользователя по его ID
   * @param idUser ID пользователя
   * @return город пользователя, или {@code empty} если не найден город указанного пользователя
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.user " +
          "INNER JOIN family_benefit.city ON family_benefit.user.id_city = family_benefit.city.id " +
          "WHERE family_benefit.user.id = ?;")
  Optional<CityEntity> findByIdUser(String idUser);

  /**
   * Возвращает город учреждения по его ID
   * @param idInstitution ID учреждения
   * @return город учреждения, или {@code empty} если не найден город указанного учреждения
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.institution " +
          "INNER JOIN family_benefit.city ON family_benefit.institution.id_city = family_benefit.city.id " +
          "WHERE family_benefit.institution.id = ?;")
  Optional<CityEntity> findByIdInstitution(String idInstitution);

  /**
   * Возвращает список городов по ID пособия
   * @param idBenefit ID пособия
   * @return список городов
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.benefits_cities " +
          "INNER JOIN family_benefit.city ON family_benefit.benefits_cities.id_city = family_benefit.city.id " +
          "WHERE family_benefit.benefits_cities.id_benefit = ?;")
  List<CityEntity> findAllByIdBenefit(String idBenefit);

  /**
   * Возвращает список полных городов по фильтру: название города, ID пособия, ID учреждения.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название города
   * @param idBenefit ID пособия
   * @param idInstitution ID учреждения
   * @return список городов, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.city.id, family_benefit.city.name, family_benefit.city.info " +
          "FROM family_benefit.benefits_cities " +
          "INNER JOIN family_benefit.city ON family_benefit.benefits_cities.id_city = family_benefit.city.id " +
          "WHERE family_benefit.benefits_cities.id_benefit = ?;")
  List<CityEntity> findAllFilter(String name, String idBenefit, String idInstitution);
}
