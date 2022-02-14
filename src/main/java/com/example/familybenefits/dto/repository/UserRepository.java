package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
  boolean existsByIdIsNotAndName(String id, String email);

  /**
   * Возвращает пользователя с ролью "ROLE_SUPER_ADMIN"
   * @return пользователь с ролью "ROLE_SUPER_ADMIN"
   */
  @Query(nativeQuery = true,
      value = "SELECT * FROM family_benefit.user " +
          "INNER JOIN family_benefit.users_roles ON family_benefit.user.id = family_benefit.users_roles.id_user " +
          "INNER JOIN family_benefit.role ON family_benefit.users_roles.id_role = family_benefit.role.id " +
          "WHERE family_benefit.role.name LIKE 'ROLE_SUPER_ADMIN';")
  UserEntity getSuperAdmin();
}
