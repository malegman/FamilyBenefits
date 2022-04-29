package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий, работающий с моделью таблицы "benefit"
 */
public interface BenefitRepository extends JpaRepository<BenefitEntity, String> {

  /**
   * Проверяет наличие пособия по его названию
   * @param name название пособия
   * @return true, если пособие с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие пособия с отличным от данного ID и данным названием
   * @param id ID пособия
   * @param name название пособия
   * @return true, если пособие с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);

  /**
   * Возвращает список пособий по ID пользователя
   * @param idUser ID пользователя
   * @return список пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.users_benefits ON family_benefit.users_benefits.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.users_benefits.id_user = ?;")
  List<BenefitEntity> findAllByIdUser(String idUser);

  /**
   * Возвращает список пособий по фильтру: название пособия, ID города, ID критерия, ID учреждения.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название пособия
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return список пособий, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefit " +
          "INNER JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE (?1 = '' OR family_benefit.benefit.name = ?1) " +
          "AND (?2 = '' OR family_benefit.benefits_cities.id_city = ?2) " +
          "AND (?3 = '' OR family_benefit.benefits_criteria.id_criterion = ?3) " +
          "AND (?4 = '' OR family_benefit.benefits_institutions.id_institution = ?4);")
  List<BenefitEntity> findAllFilter(String name, String idCity, String idCriterion, String idInstitution);

  /**
   * Возвращает список неполных пособий: без города, критерия или учреждения
   * @return список неполных критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefit " +
          "LEFT JOIN family_benefit.benefits_cities ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "LEFT JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "LEFT JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city = '' " +
          "OR family_benefit.benefits_criteria.id_criterion = '' " +
          "OR family_benefit.benefits_institutions.id_institution = '';")
  List<BenefitEntity> findAllPartial();
}
