package com.example.familybenefits.security.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

/**
 * Реализация сервиса для работы с jwt
 */
@Service
public class JwtServiceFB implements JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtServiceFB.class);

  /**
   * Закрытый ключ, используемый для подписывания jwt
   */
  @Value("$(jwt.secret)")
  private String jwtSecret;

  /**
   * Время жизни jwt в минутах
   */
  @Value("$(jwt.expirationMin)")
  private int jwtExpirationMin;

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt. null, если указан некорректный email
   */
  @Override
  public Optional<String> generateToken(String email) {

    if (!StringUtils.hasText(email)) {
      log.error("Incorrect email: [{}]", email);
      return Optional.empty();
    }

    Date expiration = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMin).toInstant(ZoneOffset.UTC));
    return Optional.of(
        Jwts.builder()
        .setSubject(email)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact()
    );
  }

  /**
   * Проверяет токен на его действительность
   * @param token проверяемый jwt
   * @return true, если токен действительный
   */
  @Override
  public boolean validateToken(String token) {

    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;

    } catch (ExpiredJwtException expEx) {
      log.error("Token expired: [{}]", token);
    } catch (UnsupportedJwtException unsEx) {
      log.error("Unsupported jwt: [{}]", token);
    } catch (MalformedJwtException mjEx) {
      log.error("Malformed jwt: [{}]", token);
    } catch (SignatureException sEx) {
      log.error("Invalid signature: [{}]", token);
    } catch (IllegalArgumentException e) {
      log.error("Invalid token: [{}]", token);
    }

    return false;
  }

  /**
   * Получает email пользователя по токену формата jwt
   * @param token токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   */
  @Override
  public Optional<String> getEmailFromToken(String token) {

    try {
      Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
      return Optional.of(claims.getSubject());

    } catch (Exception e) {
      log.error("Couldn't get email from token: [{}]", token);
      return Optional.empty();
    }
  }
}
