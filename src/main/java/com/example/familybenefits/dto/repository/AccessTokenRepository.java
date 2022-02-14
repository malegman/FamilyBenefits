package com.example.familybenefits.dto.repository;

import com.example.familybenefits.dto.entity.AccessTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий, работающий с моделью таблицы "refresh_token"
 */
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, String> {
}
