package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.exception.InvalidEmailException;
import com.example.familybenefits.security.service.s_interface.JwtService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Реализация сервиса для работы с jwt
 */
@Service
public class JwtServiceFB implements JwtService {

  /**
   * Закрытый ключ, используемый для подписывания jwt
   */
  private static final String JWT_SECRET = "familyben";

  /**
   * Время жизни jwt в минутах
   */
  private static final int JWT_EXPIRATION_MIN = 15;

  /**
   * Интерфейс сервиса, отвечающего за данные пользователя
   */
  private final UserSecurityService userSecurityService;

  @Autowired
  public JwtServiceFB(UserSecurityService userSecurityService) {
    this.userSecurityService = userSecurityService;
  }

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt. null, если указан некорректный email
   */
  @Override
  public String generateToken(String email) throws InvalidEmailException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        email, "Input value is not an email");

    Date expiration = Date.from(LocalDateTime.now().plusMinutes(JWT_EXPIRATION_MIN).toInstant(ZoneOffset.UTC));

    return Jwts.builder()
        .setSubject(email)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
        .compact();
  }

  /**
   * Преобразует строковый токен в объект токена jwt
   * @param token проверяемый токен
   * @throws RuntimeException если не удалось преобразовать токен
   */
  @Override
  public Jws<Claims> convertToJwt(String token) throws RuntimeException {

    return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
  }

  /**
   * Получает email пользователя по токену формата jwt
   * @param jwt токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   * @throws InvalidEmailException если извлеченный объект из jwt не является email
   */
  @Override
  public String getEmailFromToken(Jws<Claims> jwt) throws InvalidEmailException {

    String email = jwt.getBody().getSubject();

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        email,
        String.format("Input value %s is not an email", email));

    return email;
  }
}