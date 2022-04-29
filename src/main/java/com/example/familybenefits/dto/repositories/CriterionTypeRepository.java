package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.CriterionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
   * Возвращает список типов критерия по фильтру: название типа критерия, ID критерия.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название критерия
   * @param idCriterion ID критерия
   * @return список типов критерия, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT DISTINCT family_benefit.criterion_type.id, family_benefit.criterion_type.name, family_benefit.criterion_type.info " +
          "FROM family_benefit.criterion_type " +
          "INNER JOIN family_benefit.criterion ON family_benefit.criterion.id_type = family_benefit.criterion_type.id " +
          "WHERE (?1 = '' OR family_benefit.criterion_type.name = ?1) " +
          "AND (?2 = '' OR family_benefit.criterion.id = ?2);")
  List<CriterionTypeEntity> findAllFilter(String name, String idCriterion);

  /**
   * Возвращает список неполных типов критерия: без критериев
   * @return список критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.criterion_type.id, family_benefit.criterion_type.name, family_benefit.criterion_type.info " +
          "FROM family_benefit.criterion_type " +
          "LEFT JOIN family_benefit.criterion ON family_benefit.criterion.id_type = family_benefit.criterion_type.id " +
          "WHERE family_benefit.criterion.id = '';")
  List<CriterionTypeEntity> findAllPartial();
}
