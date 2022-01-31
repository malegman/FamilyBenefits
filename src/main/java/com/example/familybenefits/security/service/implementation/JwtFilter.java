package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.exception.InvalidEmailException;
import com.example.familybenefits.security.service.s_interface.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Фильтр для фильтрации клиентских запросов, с использованием JWT
 */
@Slf4j
@Service
public class JwtFilter extends GenericFilterBean {

  /**
   * Название заголовка запроса, для извлечения токена доступа
   */
  public static final String AUTHORIZATION = "Authorization";

  /**
   * Сервис для работы с jwt
   */
  private final JwtService jwtService;

  /**
   * Сервис для работы с объектом пользователя для авторизации
   */
  private final UserDetailsService userDetailsService;

  /**
   * Конструктор для инициализации интерфейсов сервисов
   * @param jwtService сервис для работы с jwt
   * @param userDetailsService сервис для работы с объектом пользователя, который используется для авторизации
   */
  @Autowired
  public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Фильтрует запрос клиента с использованием jwt. Определяет, авторизован ли пользователь
   * @param servletRequest запрос клиента
   * @param servletResponse ответ клиенту
   * @param filterChain объект для управления фильтрацией запроса
   * @throws IOException ошибка ввода вывода
   * @throws ServletException ошибка обработки запроса и ответа
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    try {
      // Извлечение токена из запроса, и получение пользователя на основе данных токена (email пользователя)
      String token = getTokenFromRequest((HttpServletRequest) servletRequest);
      Jws<Claims> jwt = jwtService.convertToJwt(token);
      String email = jwtService.getEmailFromToken(jwt);
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);

      // Получение данных авторизации пользователя и установка их контекст безопасности
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);

    } catch (RuntimeException | InvalidEmailException e) {
      // Ошибки извлечения токена из заброса, преобразования токена в jwt,
      // извлечения email из jwt, получения пользователя по email
      log.error(e.getMessage());

    } finally {
      // Фильтрование запроса с использованием контекста безопасности
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  /**
   * Извлекает токен из запроса клиента
   * @param request запрос клиента
   * @return токен доступа пользователя.
   * @throws IllegalArgumentException если не удалось извлечь токен
   */
  private String getTokenFromRequest(HttpServletRequest request) throws IllegalArgumentException {

    String bearer = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);

    } else {
      throw new IllegalArgumentException("Can't get token from request: " + request);
    }
  }
}
