package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.exception.InvalidEmailException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * Сервис для работы с jwt
 */
public interface JwtService {

  /**
   * Создает токен jwt пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt
   * @throws InvalidEmailException если указанный "email" не является email
   */
  String generateToken(String email) throws InvalidEmailException;

  /**
   * Преобразует строковый токен в объект токена jwt
   * @param token конвертируемый строковый токен
   * @return объект токена jwt
   * @throws RuntimeException если не удалось преобразовать токен
   */
  Jws<Claims> convertToJwt(String token) throws RuntimeException;

  /**
   * Получает email пользователя по токену формата jwt
   * @param jwt токен пользователя, jwt
   * @return email пользователя
   * @throws InvalidEmailException если извлеченный объект из jwt не является email
   */
  String getEmailFromToken(Jws<Claims> jwt) throws InvalidEmailException;
}
