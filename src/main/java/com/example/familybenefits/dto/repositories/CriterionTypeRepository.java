package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Репозиторий, работающий с моделью таблицы "criterion_type"
 */
public interface CriterionTypeRepository extends JpaRepository<CriterionTypeEntity, String> {

  /**
   * Проверяет наличие типа критерия по его названию
   * @param name название типа критерия
   * @return true, если тип критерия с указанным именем существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие типа критерия с отличным от данного ID и данным названием
   * @param id ID типа критерия
   * @param name название типа критерия
   * @return true, если тип критерия с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);

  /**
   * Возвращает тип критерия указанного критерия по его ID
   * @param idCriterion ID критерия
   * @return тип критерия указанного критерия, или {@code empty} если не найден тип критерия указанного критерия
   */
  @Query(nativeQuery = true,
      value =
          "SELECT family_benefit.criterion_type.id, family_benefit.criterion_type.name, family_benefit.criterion_type.info " +
          "FROM family_benefit.criterion " +
          "INNER JOIN family_benefit.criterion_type ON family_benefit.criterion.id_type = family_benefit.criterion_type.id " +
          "WHERE family_benefit.criterion.id = ?;")
  Optional<CriterionTypeEntity> findByIdCriterion(String idCriterion);
}
