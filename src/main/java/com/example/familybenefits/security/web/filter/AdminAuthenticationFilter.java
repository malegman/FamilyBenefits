package com.example.familybenefits.security.web.filter;

import com.example.familybenefits.dto.repository.AccessTokenRepository;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Реализация базового фильтра, основанного на токене формата jwt.
 * Фильтрует запросы, связанные с пользователем, и с путем "/admins**"
 */
@Component
public class AdminAuthenticationFilter extends BasicJwtAuthenticationFilter {

  /**
   * Конструктор для инициализации репозитория
   * @param accessTokenRepository репозиторий, работающий с моделью таблицы "access_token"
   */
  public AdminAuthenticationFilter(AccessTokenRepository accessTokenRepository) {
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

    return notEqualsIp(userAuth, request);
  }
}
