package com.example.familybenefits.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
 * Фильтр для фильрации клиентских запросов, с использованием JWT
 */
@Service
public class JwtFilter extends GenericFilterBean {

  /**
   * Название заголовка запроса, для извлечния токена доступа
   */
  public static final String AUTHORIZATION = "Authorization";

  /**
   * Сервис для работы с jwt
   */
  @NonNull
  private final JwtService jwtService;

  /**
   * Сервис для работы с объектом пользователя для авторизации
   */
  @NonNull
  private final UserDetailsService userDetailsService;

  /**
   * Конструктор для инициализации интерфейсов сервисов
   * @param jwtService сервис для работы с jwt
   * @param userDetailsService сервис для работы с объектом пользователя, который используется для авторизации
   */
  @Autowired
  public JwtFilter(@NonNull JwtService jwtService, @NonNull UserDetailsService userDetailsService) {
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
  public void doFilter(@NonNull ServletRequest servletRequest, @NonNull ServletResponse servletResponse, @NonNull FilterChain filterChain) throws IOException, ServletException {

    //log.info("do filter...");
    String token = getTokenFromRequest((HttpServletRequest) servletRequest);

    if (token != null && jwtService.validateToken(token)) {
      String userLogin = jwtService.getEmailFromToken(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  /**
   * Извлекает токен из запроса клиента
   * @param request запрос клиента
   * @return токен пользователя, jwt
   */
  @Nullable
  private String getTokenFromRequest(@NonNull HttpServletRequest request) {

    String bearer = request.getHeader(AUTHORIZATION);

    if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }

    return null;
  }
}
