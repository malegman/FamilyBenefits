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
      value = "SELECT DISTINCT family_benefit.criterion.id, family_benefit.criterion.name, family_benefit.criterion.info, family_benefit.criterion.id_type " +
          "FROM family_benefit.users_criteria " +
          "INNER JOIN family_benefit.criterion ON family_benefit.users_criteria.id_criterion = family_benefit.criterion.id " +
          "WHERE family_benefit.users_criteria.id_user = ?;")
  List<CriterionEntity> findAllByIdUser(String idUser);

  /**
   * Возвращает список критерий по фильтру: название критерия, ID пособия, ID типа критерия.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название пособия
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return список критерий, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT DISTINCT family_benefit.criterion.id, family_benefit.criterion.name, family_benefit.criterion.info, family_benefit.criterion.id_type " +
          "FROM family_benefit.criterion " +
          "INNER JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_criterion = family_benefit.criterion.id " +
          "WHERE (?1 = '' OR family_benefit.criterion.name = ?1) " +
          "AND (?2 = '' OR family_benefit.benefits_criteria.id_benefit = ?2) " +
          "AND (?3 = '' OR family_benefit.criterion.id_type = ?3);")
  List<CriterionEntity> findAllFilter(String name, String idBenefit, String idCriterionType);

  /**
   * Возвращает список неполных критериев: без пособия
   * @return список критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.criterion.id, family_benefit.criterion.name, family_benefit.criterion.info, family_benefit.criterion.id_type " +
          "FROM family_benefit.criterion " +
          "LEFT JOIN family_benefit.benefits_criteria ON family_benefit.benefits_criteria.id_criterion = family_benefit.city.id " +
          "WHERE family_benefit.benefits_criteria.id_benefit = '';")
  List<CriterionEntity> findAllPartial();
}
