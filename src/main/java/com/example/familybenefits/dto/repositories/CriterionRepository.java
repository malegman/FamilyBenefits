package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.CriterionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий, работающий с моделью таблицы "criterion"
 */
public interface CriterionRepository extends JpaRepository<CriterionEntity, String> {

  /**
   * Проверяет наличие критерия по его названию
   * @param name название критерия
   * @return true, если критерий с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие критерия с отличным от данного ID и данным названием
   * @param id ID критерия
   * @param name название критерия
   * @return true, если критерий с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);

  /**
   * Возвращает список критерий по ID пользователя
   * @param idUser ID пользователя
   * @return список критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.criterion.id, family_benefit.criterion.name, family_benefit.criterion.info, family_benefit.criterion.id_type " +
          "FROM family_benefit.users_criteria " +
          "INNER JOIN family_benefit.criterion ON family_benefit.users_criteria.id_criterion = family_benefit.criterion.id " +
          "WHERE family_benefit.users_criteria.id_user = ?;")
  List<CriterionEntity> findAllByIdUser(String idUser);

  /**
   * Возвращает список критерий по ID пособия
   * @param idBenefit ID пособия
   * @return список критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.criterion.id, family_benefit.criterion.name, family_benefit.criterion.info, family_benefit.criterion.id_type " +
          "FROM family_benefit.benefits_criteria " +
          "INNER JOIN family_benefit.criterion ON family_benefit.benefits_criteria.id_criterion = family_benefit.criterion.id " +
          "WHERE family_benefit.benefits_criteria.id_benefit = ?;")
  List<CriterionEntity> findAllByIdBenefit(String idBenefit);

  /**
   * Возвращает список критерий по ID города
   * @param idCriterionType ID города
   * @return список критерий
   */
  List<CriterionEntity> findAllByIdCriterionType(String idCriterionType);
}
