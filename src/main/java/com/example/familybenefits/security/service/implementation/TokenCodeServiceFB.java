package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.dto.entity.RoleEntity;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import com.example.familybenefits.security.service.s_interface.TokenCodeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с токеном доступа (в формате jwt) и кодом для входа
 */
@Service
public class TokenCodeServiceFB implements TokenCodeService {

  /**
   * Длина кода для входа в систему
   */
  public static final int LOGIN_CODE_LENGTH = 6;

  /**
   * Генерирует jwt для пользователя на основе его ID, ролей и IP-адреса запроса на вход систему
   * @param id ID пользователя
   * @param roleEntitySet множество ролей пользователя
   * @param request http запрос на вход систему
   * @return сгенерированный jwt
   */
  @Override
  public String generateJwt(String id, Set<RoleEntity> roleEntitySet, HttpServletRequest request) {

    Date expiration = Date.from(LocalDateTime.now().plusSeconds(R.JWT_EXPIRATION_SEC).toInstant(ZoneOffset.UTC));

    return Jwts.builder()
        .setSubject(JwtAuthenticationUserData
                        .builder()
                        .idUser(id)
                        .nameRoleSet(roleEntitySet
                                         .stream()
                                         .map(RoleEntity::getName)
                                         .collect(Collectors.toSet()))
                        .ipAddress(request.getRemoteAddr())
                        .build()
                        .toString())
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, R.JWT_SECRET)
        .compact();
  }

  /**
   * Преобразует строковый токен в объект токена jwt
   * @param token конвертируемый строковый токен
   * @return токен в формате jwt
   * @throws RuntimeException если не удалось преобразовать токен
   */
  @Override
  public Jws<Claims> toJwt(String token) throws RuntimeException {

    return Jwts.parser().setSigningKey(R.JWT_SECRET).parseClaimsJws(token);
  }

  /**
   * Получает данные авторизации пользователя по токену формата jwt
   * @param jwt токен доступа пользователя в формате jwt
   * @return данные авторизации
   */
  @Override
  public JwtAuthenticationUserData authFromJwt(Jws<Claims> jwt) {

    return JwtAuthenticationUserData.fromString(jwt.getBody().getSubject());
  }

  /**
   * Создает код для входа в систему.
   * Код представляет собой 6-ти значное число
   * @return сгенерированный код
   */
  @Override
  public int generateLoginCode() {

    byte[] randBytes = new byte[LOGIN_CODE_LENGTH];
    int loginCode = 0;

    // Для пропорционального приведения диапазона [0-255] к диапазону [0-9]
    double part = 10 / 255.0;

    // Получение случайных значений
    (new SecureRandom()).nextBytes(randBytes);

    // "Заполнение" числа цифрами
    for (int randI = 0, tempVal = 1; randI < LOGIN_CODE_LENGTH; randI++, tempVal *= 10) {
      loginCode += tempVal * (int) Math.floor((randBytes[randI] + 127) * part);
    }

    return loginCode;
  }
}
