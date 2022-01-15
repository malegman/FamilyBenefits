package com.example.familybenefits.security.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Сервис для работы с jwt
 */
public interface JwtService {

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt
   */
  @NonNull
  String generateToken(@NonNull String email);

  /**
   * Проверяет токен на его действительность
   * @param token проверяемый jwt
   * @return true, если токен действительный
   */
  boolean validateToken(@NonNull String token);

  /**
   * Получает email пользователя по токену
   * @param token токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   */
  @Nullable
  String getEmailFromToken(@NonNull String token);
}
