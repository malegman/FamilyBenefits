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
   * Возвращает список учреждений по ID пособия
   * @param idBenefit ID пособия
   * @return список критерий
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.institution.id, family_benefit.institution.name, family_benefit.institution.info, family_benefit.institution.address, " +
          "family_benefit.institution.phone, family_benefit.institution.email, family_benefit.institution.schedule, family_benefit.institution.id_city " +
          "FROM family_benefit.benefits_institutions " +
          "INNER JOIN family_benefit.institution ON family_benefit.benefits_institutions.id_institution = family_benefit.institution.id " +
          "WHERE family_benefit.benefits_institutions.id_benefit = ?;")
  List<InstitutionEntity> findAllByIdBenefit(String idBenefit);

  /**
   * Возвращает список учреждений по ID города
   * @param idCity ID города
   * @return список учреждений
   */
  List<InstitutionEntity> findAllByIdCity(String idCity);
}
