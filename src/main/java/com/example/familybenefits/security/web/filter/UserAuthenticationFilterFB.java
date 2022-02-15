package com.example.familybenefits.security.web.filter;

import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Реализация базового фильтра, основанного на токене формата jwt.
 * Фильтрует запросы, связанные с пользователем, и с путем "/users**"
 */
@Component
public class UserAuthenticationFilterFB extends BasicJwtAuthenticationFilterFB {

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
          (requestMethod.equals("POST") && requestURI.equals("/users")) ||
          (requestMethod.equals("GET")  && requestURI.equals("/users/init-data"))
      );
    }

    return notEqualsIp(userAuth, request);
  }
}
