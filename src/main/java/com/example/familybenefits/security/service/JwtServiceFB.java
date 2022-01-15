package com.example.familybenefits.security.service;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
  @NonNull
  @Value("$(jwt.secret)")
  private String jwtSecret;

  /**
   * Время жизни jwt в минутах
   */
  @NonNull
  @Value("$(jwt.expirationMin)")
  private int jwtExpirationMin;

  /**
   * Создает токен пользователя с использованием его email
   * @param email email пользователя
   * @return сгенерированный jwt
   */
  @Override
  @NotNull
  public String generateToken(@NonNull String email) {

    Date expiration = Date.from(LocalDateTime.now().plusMinutes(jwtExpirationMin).toInstant(ZoneOffset.UTC));
    return Jwts.builder()
        .setSubject(email)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  /**
   * Проверяет токен на его действительность
   * @param token проверяемый jwt
   * @return true, если токен действительный
   */
  @Override
  public boolean validateToken(@NonNull String token) {

    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;

    } catch (ExpiredJwtException expEx) {
      // log.severe("Token expired");
    } catch (UnsupportedJwtException unsEx) {
      // log.severe("Unsupported jwt");
    } catch (MalformedJwtException mjEx) {
      // log.severe("Malformed jwt");
    } catch (SignatureException sEx) {
      // log.severe("Invalid signature");
    } catch (IllegalArgumentException e) {
      // log.severe("invalid token");
    }

    return false;
  }

  /**
   * Получает email пользователя по токену
   * @param token токен пользователя, jwt
   * @return email пользователя. null, если не удалось извлечь email из jwt
   */
  @Override
  @Nullable
  public String getEmailFromToken(@NonNull String token) {

    try {
      Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
      return claims.getSubject();

    } catch (ExpiredJwtException expEx) {
      // log.severe("Token expired");
    } catch (UnsupportedJwtException unsEx) {
      // log.severe("Unsupported jwt");
    } catch (MalformedJwtException mjEx) {
      // log.severe("Malformed jwt");
    } catch (SignatureException sEx) {
      // log.severe("Invalid signature");
    } catch (Exception e) {
      // log.severe("invalid token");
    }

    return null;
  }
}
