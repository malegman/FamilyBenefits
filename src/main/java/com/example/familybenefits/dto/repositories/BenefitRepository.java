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
          "FROM family_benefit.users_benefits " +
          "INNER JOIN family_benefit.benefit ON family_benefit.users_benefits.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.users_benefits.id_user = ?;")
  List<BenefitEntity> findAllByIdUser(String idUser);

  /**
   * Возвращает список пособий по ID города
   * @param idCity ID города
   * @return список пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefits_cities " +
          "INNER JOIN family_benefit.benefit ON family_benefit.benefits_cities.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_cities.id_city = ?;")
  List<BenefitEntity> findAllByIdCity(String idCity);

  /**
   * Возвращает список пособий по ID критерия
   * @param idCriterion ID критерия
   * @return список пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefits_criteria " +
          "INNER JOIN family_benefit.benefit ON family_benefit.benefits_criteria.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_criteria.id_criterion = ?;")
  List<BenefitEntity> findAllByIdCriterion(String idCriterion);

  /**
   * Возвращает список пособий по ID учреждения
   * @param idInstitution ID учреждения
   * @return список пособий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.benefit.id, family_benefit.benefit.name, family_benefit.benefit.info, family_benefit.benefit.documents " +
          "FROM family_benefit.benefits_institutions " +
          "INNER JOIN family_benefit.benefit ON family_benefit.benefits_institutions.id_benefit = family_benefit.benefit.id " +
          "WHERE family_benefit.benefits_institutions.id_institution = ?;")
  List<BenefitEntity> findAllByIdInstitution(String idInstitution);
}
