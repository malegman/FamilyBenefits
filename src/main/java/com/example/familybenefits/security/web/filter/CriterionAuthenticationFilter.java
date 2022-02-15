package com.example.familybenefits.security.web.filter;

import com.example.familybenefits.dto.repository.AccessTokenRepository;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Реализация базового фильтра, основанного на токене формата jwt.
 * Фильтрует запросы, связанные с критерием, и с путем "/criteria**"
 */
@Component
public class CriterionAuthenticationFilter extends BasicJwtAuthenticationFilter {

  /**
   * Конструктор для инициализации репозитория
   * @param accessTokenRepository репозиторий, работающий с моделью таблицы "access_token"
   */
  public CriterionAuthenticationFilter(AccessTokenRepository accessTokenRepository) {
    super(accessTokenRepository);
  }

  /**
   * Проверяет запрос на аутентификацию по запросу и данным пользователя из jwt
   * @param userAuth данные пользователя из jwt
   * @param request проверяемый запрос
   * @return true, если запрос не аутентифицированный
   */
  @Override
  protected boolean notAuthenticated(JwtAuthenticationUserData userAuth, HttpServletRequest request) {

    String requestURI = request.getRequestURI();
    String requestMethod = request.getMethod();

    // Разрешение запросов, которые не требуют авторизации
    if (userAuth == null) {
      return !(
          (requestMethod.equals("GET") && requestURI.equals("/criteria")) ||
          (requestMethod.equals("GET") && requestURI.equals("/criteria/{id}"))
      );
    }

    return notEqualsIp(userAuth, request);
  }
}
