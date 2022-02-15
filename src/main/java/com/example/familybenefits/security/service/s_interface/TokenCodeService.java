package com.example.familybenefits.security.service.s_interface;

import com.example.familybenefits.dto.entity.RoleEntity;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Сервис для работы с токеном доступа (в формате jwt) и кодом для входа
 */
public interface TokenCodeService {

  /**
   * Генерирует jwt для пользователя на основе его ID, ролей и IP-адреса запроса на вход систему
   * @param id ID пользователя
   * @param roleEntitySet множество ролей пользователя
   * @param request http запрос на вход систему
   * @return сгенерированный jwt
   */
  String generateJwt(String id, Set<RoleEntity> roleEntitySet, HttpServletRequest request);

  /**
   * Преобразует строковый токен в объект токена jwt
   * @param token конвертируемый строковый токен
   * @return объект токена jwt
   * @throws RuntimeException если не удалось преобразовать токен
   */
  Jws<Claims> toJwt(String token) throws RuntimeException;

  /**
   * Получает данные авторизации пользователя по токену формата jwt
   * @param jwt токен пользователя, jwt
   * @return данные авторизации
   */
  JwtAuthenticationUserData authFromJwt(Jws<Claims> jwt);

  /**
   * Создает код для входа в систему.
   * Код представляет собой 6-ти значное число
   * @return сгенерированный код
   */
  int generateLoginCode();
}