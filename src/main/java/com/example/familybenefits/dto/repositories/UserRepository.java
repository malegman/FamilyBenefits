package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Репозиторий, работающий с моделью таблицы "user"
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {

  /**
   * Находит пользователя по email
   * @param email email пользователя
   * @return пользователь или empty, если пользователь не найден
   */
  Optional<UserEntity> findByEmail(String email);

  /**
   * Проверяет наличие пользователя с указанным email
   * @param email email пользователя
   * @return true, если пользователь с указанным email существует
   */
  boolean existsByEmail(String email);

  /**
   * Проверяет наличие пользователя с отличным от данного ID и данным email
   * @param id ID пользователя
   * @param email email пользователя
   * @return true, если пользователь с отличным ID и указанным email существует
   */
  boolean existsByIdIsNotAndEmail(String id, String email);

  /**
   * Возвращает пользователя с ролью "ROLE_SUPER_ADMIN"
   * @return пользователь с ролью "ROLE_SUPER_ADMIN"
   */
  @Query(nativeQuery = true,
      value = "SELECT * " +
          "FROM family_benefit.user " +
          "INNER JOIN family_benefit.users_roles ON family_benefit.user.id = family_benefit.users_roles.id_user " +
          "INNER JOIN family_benefit.role ON family_benefit.users_roles.id_role = family_benefit.role.id " +
          "WHERE family_benefit.role.name LIKE 'ROLE_SUPER_ADMIN';")
  UserEntity getSuperAdmin();

  /**
   * Создает связь между существующими датой рождения ребенка и пользователем, по их ID
   * @param idUser ID пользователя
   * @param idChildBirth ID рождения ребенка
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "INSERT INTO family_benefit.users_child_births (id_user, id_child_birth) VALUES (?1, ?2);")
  void addChild(String idUser, String idChildBirth);

  /**
   * Удаляет связь между датами рождениями детей и пользователем по его ID
   * @param idUser ID пользователя
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "DELETE FROM family_benefit.users_child_births WHERE (id_user = ?1);")
  void deleteAllChildBirths(String idUser);

  /**
   * Проверяет наличие роли у пользователя по их ID
   * @param idUser ID пользователя
   * @param idRole ID роли
   * @return true, если найден пользователь с указанной ролью
   */
  @Query(nativeQuery = true,
      value = "SELECT EXISTS(SELECT * FROM family_benefit.users_roles WHERE (id_user = ?1 AND id_role = ?2))")
  boolean hasUserRole(String idUser, String idRole);

  /**
   * Создает связь между существующими ролью и пользователем, по их ID
   * @param idUser ID пользователя
   * @param idRole ID роли
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "INSERT INTO family_benefit.users_roles (id_user, id_role) VALUES (?1, ?2);")
  void addRole(String idUser, String idRole);

  /**
   * Удаляет связь между существующими ролью и пользователем, по их ID
   * @param idUser ID пользователя
   * @param idRole ID роли
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "DELETE FROM family_benefit.users_roles WHERE (id_user = ?1 AND id_role = ?2);")
  void deleteRole(String idUser, String idRole);

  /**
   * Создает связь между существующими пособием и пользователем, по их ID
   * @param idUser ID пользователя
   * @param idBenefit ID пособия
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "INSERT INTO family_benefit.users_benefits (id_user, id_benefit) VALUES (?1, ?2);")
  void addBenefit(String idUser, String idBenefit);

  /**
   * Удаляет связь между пособиями и пользователем по его ID
   * @param idUser ID пользователя
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "DELETE FROM family_benefit.users_benefits WHERE id_user = ?1;")
  void deleteAllBenefits(String idUser);

  /**
   * Создает связь между существующими критерием и пользователем, по их ID
   * @param idUser ID пользователя
   * @param idCriterion ID критерия
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "INSERT INTO family_benefit.users_criteria (id_user, id_criterion) VALUES (?1, ?2);")
  void addCriterion(String idUser, String idCriterion);

  /**
   * Удаляет связь между критериями и пользователем по его ID
   * @param idUser ID пользователя
   */
  @Modifying
  @Query(nativeQuery = true,
      value = "DELETE FROM family_benefit.users_criteria WHERE id_user = ?1;")
  void deleteAllCriteria(String idUser);
}
