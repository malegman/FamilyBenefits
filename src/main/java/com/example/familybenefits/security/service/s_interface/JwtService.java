package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.exception.InvalidEmailException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * Сервис для работы с jwt
 */
public interface JwtService {

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt. null, если указан некорректный email
   * @throws InvalidEmailException если указанный "email" не является email
   */
  String generateToken(String email) throws InvalidEmailException;

  /**
   * Проверяет токен на его действительность
   * @param token проверяемый jwt
   * @throws RuntimeException если токен недействительный
   */
  Jws<Claims> convertToJwt(String token) throws RuntimeException;

  /**
   * Получает email пользователя по токену формата jwt
   * @param jwt токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   * @throws InvalidEmailException если извлеченный объект из jwt не является email
   */
  String getEmailFromToken(Jws<Claims> jwt) throws InvalidEmailException;
}
