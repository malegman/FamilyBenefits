package com.example.familybenefits.security.web.filter;

import com.example.familybenefits.dto.repository.AccessTokenRepository;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationToken;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Базовый фильтр, основанный на токене формата jwt.
 * В реализациях фильтра необходимо реализовать метод для проверки аутентификации запроса.
 * Метод используется при фильтрации.
 * В случае провала аутентификации, в ответ помещается статус 401.
 */
@Slf4j
public abstract class BasicJwtAuthenticationFilter extends HttpFilter {

  /**
   * Репозиторий, работающий с моделью таблицы "access_token"
   */
  private final AccessTokenRepository accessTokenRepository;

  /**
   * Конструктор для инициализации репозитория
   * @param accessTokenRepository репозиторий, работающий с моделью таблицы "access_token"
   */
  @Autowired
  public BasicJwtAuthenticationFilter(AccessTokenRepository accessTokenRepository) {
    this.accessTokenRepository = accessTokenRepository;
  }

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

    // Получение данных аутентификации
    JwtAuthenticationUserData userAuth = new JwtAuthenticationUserData();
    String jwt = jwtFromRequest(request);
    // Если обнаружен токен доступа в заголовке запроса
    if (jwt != null) {
      try {
        userAuth = authFromJwt(jwt);

      } catch (RuntimeException e) {
        // Если не удалось извлечь из токена доступа jwt
        log.error("{} {} \"{}\" : {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), e.getMessage());
      }
    }

    // Проверка существования токена доступа и данных аутентификации. Если токена нет или запрос не прошел аутентификацию, то 401
    if (!accessTokenRepository.existsById(userAuth.getIdUser()) || notAuthenticated(userAuth, request)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // Установка данных авторизации в контекст
    JwtAuthenticationToken auth = new JwtAuthenticationToken(userAuth, jwt, userAuth
        .getNameRoleSet()
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet()));
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
    if (StringUtils.hasText(bearer) && bearer.startsWith(R.ACCESS_TOKEN_PREFIX)) {
      return bearer.substring(R.ACCESS_TOKEN_PREFIX.length());
    }

    return null;
  }

  /**
   * Извлекает данные пользователя из jwt.
   * @param jwt jwt пользователя
   * @return данные пользователя
   * @throws RuntimeException если не удалось извлечь данные пользователя из токена
   */
  private JwtAuthenticationUserData authFromJwt(String jwt) throws RuntimeException {

    return JwtAuthenticationUserData.fromString(
        Jwts.parser()
            .setSigningKey(R.JWT_SECRET)
            .parseClaimsJws(jwt)
            .getBody()
            .getSubject());
  }
}
