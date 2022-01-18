package com.example.familybenefits.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Фильтр для фильрации клиентских запросов, с использованием JWT
 */
@Service
public class JwtFilter extends GenericFilterBean {

  private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

  /**
   * Название заголовка запроса, для извлечния токена доступа
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

    String token;
    String email;

    Optional<String> optToken = getTokenFromRequest((HttpServletRequest) servletRequest);

    if (optToken.isEmpty()) {
      ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      log.warn("Couldn't get token from request: [{}]", servletRequest);
    } else {
      token = optToken.get();
      if (jwtService.validateToken(token)) {
        Optional<String> optEmail = jwtService.getEmailFromToken(token);
        if (optEmail.isEmpty()) {
          ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          log.warn("Couldn't get email from token: [{}]", token);
        } else {
          email = optEmail.get();
          UserDetails userDetails = null;
          try {
            userDetails = userDetailsService.loadUserByUsername(email);
          } catch (UsernameNotFoundException e) {
            ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("UserDetails with email [{}] not found", email);
          }
          if (userDetails != null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
          }
        }
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * Извлекает токен из запроса клиента
   * @param request запрос клиента
   * @return токен пользователя, jwt. null, если токен не обнаружен или request == null
   */
  private Optional<String> getTokenFromRequest(HttpServletRequest request) {

    if (request == null) {
      return Optional.empty();
    }

    String bearer = request.getHeader(AUTHORIZATION);

    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return Optional.of(bearer.substring(7));
    }

    return Optional.empty();
  }
}
