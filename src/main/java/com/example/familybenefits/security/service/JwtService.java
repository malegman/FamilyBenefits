package com.example.familybenefits.security.service;

import java.util.Optional;

/**
 * Сервис для работы с jwt
 */
public interface JwtService {

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt. null, если указан некорректный email
   */
  Optional<String> generateToken(String email);

  /**
   * Проверяет токен на его действительность
   * @param token проверяемый jwt
   * @return true, если токен действительный
   */
  boolean validateToken(String token);

  /**
   * Получает email пользователя по токену формата jwt
   * @param token токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   */
  Optional<String> getEmailFromToken(String token);
}
