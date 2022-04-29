package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.ChildBirthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий, работающий с моделью таблицы "child_birth"
 */
public interface ChildBirthRepository extends JpaRepository<ChildBirthEntity, String> {

  /**
   * Находит рождение ребенка по дате рождения
   * @param dateBirth дата рождения
   * @return модель рождения ребенка или {@code empty}, если рождение не найдено
   */
  Optional<ChildBirthEntity> findByDateBirth(LocalDate dateBirth);

  /**
   * Возвращает список рождений детей по ID пользователя
   * @param idUser ID пользователя
   * @return список рождений детей
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.child_birth.id, family_benefit.child_birth.date_birth " +
          "FROM family_benefit.users_child_births " +
          "INNER JOIN family_benefit.child_birth ON family_benefit.users_child_births.id_child_birth = family_benefit.child_birth.id " +
          "WHERE family_benefit.users_child_births.id_user = ?;")
  List<ChildBirthEntity> findAllByIdUser(String idUser);
}
