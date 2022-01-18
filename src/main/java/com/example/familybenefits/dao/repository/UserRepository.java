package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Репозиторий, работающий с моделью таблицы "user"
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, BigInteger> {

  /**
   * Находит пользователя по email
   * @param email email пользователя
   * @return пользователь или empty, если пользователь не найден
   */
  Optional<UserEntity> findByEmail(String email);
}
