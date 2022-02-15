package com.example.familybenefits.security.web.filter;

import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationToken;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Базовый фильтр, основанный на токене формата jwt.
 * В реализациях фильтра необходимо реализовать метод для проверки аутентификации запроса.
 * Метод используется при фильтрации.
 * В случае провала аутентификации, в ответ помещается статус 401.
 */
public abstract class BasicJwtAuthenticationFilterFB extends HttpFilter {

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    // Получение данных аутентификации
    String jwt = jwtFromRequest(request);
    JwtAuthenticationUserData userAuth = authFromJwt(jwt).orElse(null);

    // Проверка данных аутентификации. Если запрос не прошел аутентификацию, то 401
    if (notAuthenticated(userAuth, request)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // Установка данных авторизации в контекст, если они есть. Иначе пустая коллекция
    Set<SimpleGrantedAuthority> grantedAuthorities = Collections.emptySet();
    if (userAuth != null) {
      grantedAuthorities = userAuth
          .getNameRoleSet()
          .stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    }
    JwtAuthenticationToken auth = new JwtAuthenticationToken(userAuth, jwt, grantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(auth);

    // Передача запроса следующему фильтру в цепочке
    filterChain.doFilter(request, response);
  }

  /**
   * Проверяет запрос на отсутствие аутентификации по запросу и данным пользователя из jwt
   * @param userAuth данные пользователя из jwt
   * @param request проверяемый запрос
   * @return true, если запрос не аутентифицированный
   */
  protected abstract boolean notAuthenticated(JwtAuthenticationUserData userAuth, HttpServletRequest request);

  /**
   * Проверяет не эквивалентность ip-адресов текущего запроса и пользователя
   * @param userAuth данные пользователя, содержащие ip-адрес
   * @param request текущий запрос для проверки
   * @return true, если {@code userAuth == null || request == null} или адреса не эквивалентны
   */
  protected boolean notEqualsIp(JwtAuthenticationUserData userAuth, HttpServletRequest request) {

    if (userAuth != null && request != null) {
      return !userAuth.getIpAddress().equals(request.getRemoteAddr());
    }

    return true;
  }

  /**
   * Извлекает токен из запроса клиента. null, если токен не найден
   * @param request запрос клиента
   * @return токен доступа пользователя.
   */
  private String jwtFromRequest(HttpServletRequest request) {

    if (request == null) {
      return null;
    }

    String bearer = request.getHeader(R.AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);

    } else {
      return null;
    }
  }

  /**
   * Извлекает данные пользователя из jwt.
   * Empty, если не удалось извлечь данные
   * @param jwt jwt пользователя
   * @return данные пользователя
   */
  private Optional<JwtAuthenticationUserData> authFromJwt(String jwt) {

    if (jwt == null) {
      return Optional.empty();
    }

    try {
      return Optional.of(
          JwtAuthenticationUserData.fromString(
              Jwts.parser()
                  .setSigningKey(R.JWT_SECRET)
                  .parseClaimsJws(jwt)
                  .getBody()
                  .getSubject()));

    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
