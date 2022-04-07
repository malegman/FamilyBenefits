package com.example.familybenefits.part_auth.filter.request_handlers;

import com.example.familybenefits.part_auth.HttpHeadersSupport;
import com.example.familybenefits.part_auth.models.JwtUserData;
import com.example.familybenefits.part_auth.services.interfaces.AuthService;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.resources.RDB;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обрабатывает запросы вида "/api/users**" на основе их данных аутентификации и авторизации.
 */
@Component
public class UserRequestHandler {

  /**
   * Шаблон для проверки соответствия и извлечения параметра (id) из запроса "/api/users/(id)"
   */
  private static final Pattern PATTERN_USERS_ID = Pattern.compile(String.format(
      "^/api/users/(?<id>[A-Za-z0-9]{%s})$", R.ID_LENGTH));

  /**
   * Интерфейс сервиса, отвечающего за аутентификацию и авторизацию в системе
   */
  private final AuthService authService;

  /**
   * Конструктор для инициализации сервисов
   * @param authService интерфейс сервиса, отвечающего за аутентификацию и авторизацию в системе
   */
  public UserRequestHandler(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Обрабатывает http запрос вида "/api/users**" и изменяет http ответ. Ответ может быть изменен в следующих случаях:
   * <ol>
   *   <li>Запрос не прошел проверку на аутентификацию и авторизацию. В ответ записывается 401 или 403 код статуса.</li>
   *   <li>Запрос на вход или выход. Необходимо установить или удалить токены.</li>
   *   <li>Запрос содержит просроченный токен доступа. В ответ записываются обновленные токены.</li>
   *   <li>Запрос содержит просроченный токен восстановления или невалидные токены. Из ответа удаляются токены.</li>
   *   <li>API не поддерживает конечную точку, указанную в запросе. В ответ записывается 405 код статуса.</li>
   * </ol>
   * @param request http запрос
   * @param response http ответ
   * @return true, если запрос успешно обработан
   */
  public boolean handle(HttpServletRequest request, HttpServletResponse response) {

    String requestURI = request.getRequestURI();
    String requestMethod = request.getMethod();

    // Разрешение запросов, которые доступны всем
    if (requestMethod.equals("GET") && requestURI.equals("/api/users/init-data")) {
      return true;
    }

    // Разрешение запросов для анонимных пользователей
    if (requestMethod.equals("POST") && requestURI.equals("/api/users")) {

      // Проверка отсутствия аутентификации по наличию токена восстановления
      if (HttpHeadersSupport.getRefreshToken(request) == null) {
        return true;
      }
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return false;
    }

    Matcher matcherUsersId = PATTERN_USERS_ID.matcher(requestURI);

    // Проверка аутентификации и авторизации для запросов, которые для авторизованных пользователей
    if ((requestMethod.equals("GET") || requestMethod.equals("PUT") || requestMethod.equals("DELETE")) &&
        matcherUsersId.matches()) {

      // Проверка аутентификации по токенам доступа (jwt) и восстановления из запроса
      Optional<JwtUserData> optUserData = authService.authenticate(request, response);
      if (optUserData.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
      }
      JwtUserData userData = optUserData.get();

      // Проверка авторизации по наличию необходимых ролей
      if (!userData.hasRole(List.of(RDB.ROLE_USER)) ||
          !userData.getIdUser().equals(matcherUsersId.group("id"))) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
      }
      return true;
    }

    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    return false;
  }
}

