package com.example.familybenefits.dto.repositories;

import com.example.familybenefits.dto.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий, работающий с моделью таблицы "role"
 */
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

  /**
   * Возвращает список ролей по ID пользователя
   * @param idUser ID пользователя
   * @return список ролей
   */
  @Query(nativeQuery = true,
      value = "SELECT family_benefit.role.id, family_benefit.role.name " +
          "FROM family_benefit.users_roles " +
          "INNER JOIN family_benefit.role ON family_benefit.users_roles.id_role = family_benefit.role.id " +
          "WHERE family_benefit.users_roles.id_user = ?;")
  List<RoleEntity> findAllByIdUser(String idUser);
}

