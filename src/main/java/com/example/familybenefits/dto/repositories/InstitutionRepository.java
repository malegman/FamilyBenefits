package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий, работающий с моделью таблицы "institution"
 */
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, String> {

  /**
   * Проверяет наличие учреждения по его названию
   * @param name название учреждения
   * @return true, если учреждение с указанным названием существует
   */
  boolean existsByName(String name);

  /**
   * Проверяет наличие учреждения с отличным от данного ID и данным названием
   * @param id ID учреждения
   * @param name название учреждения
   * @return true, если учреждение с отличным ID и указанным названием существует
   */
  boolean existsByIdIsNotAndName(String id, String name);

  /**
   * Возвращает список учреждений по фильтру: название учреждения, ID города, ID пособия.
   * Если в качестве параметра указан {@code null}, то параметр бд сравнивается {@code != null}
   * @param name название учреждения
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return список учреждений, удовлетворяющих параметрам
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.institution.id, family_benefit.institution.name, family_benefit.institution.info, family_benefit.institution.address, " +
          "family_benefit.institution.phone, family_benefit.institution.email, family_benefit.institution.schedule, family_benefit.institution.id_city " +
          "FROM family_benefit.institution " +
          "INNER JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_institution = family_benefit.institution.id " +
          "WHERE (?1 IS NULL OR family_benefit.institution.name = ?1) " +
          "AND (?2 IS NULL OR family_benefit.institution.id_city = ?2) " +
          "AND (?3 IS NULL OR family_benefit.benefits_criteria.id_benefit = ?3);")
  List<InstitutionEntity> findAllFilter(String name, String idCity, String idBenefit);

  /**
   * Возвращает список неполных учреждений: без пособия
   * @return список неполных учреждений
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.institution.id, family_benefit.institution.name, family_benefit.institution.info, family_benefit.institution.address, " +
          "family_benefit.institution.phone, family_benefit.institution.email, family_benefit.institution.schedule, family_benefit.institution.id_city " +
          "FROM family_benefit.institution " +
          "LEFT JOIN family_benefit.benefits_institutions ON family_benefit.benefits_institutions.id_institution = family_benefit.institution.id " +
          "WHERE family_benefit.benefits_institutions.id_benefit IS NULL;")
  List<InstitutionEntity> findAllPartial();

}
