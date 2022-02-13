package com.example.familybenefits.dao.repository;

import com.example.familybenefits.dao.entity.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "refresh_token"
 */
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, String> {
}
