package com.example.familybenefits.part_auth.filter;

import com.example.familybenefits.part_auth.filter.request_handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр всех входящих http запросов
 */
@Component
public class AllRequestsFilterFB extends OncePerRequestFilter {

  /**
   * Обрабатывает запросы вида "/api/admins**" на основе их данных аутентификации и авторизации.
   */
  private final AdminRequestHandler adminRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/auth**" на основе их данных аутентификации и авторизации.
   */
  private final AuthRequestHandler authRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/cities**" на основе их данных аутентификации и авторизации.
   */
  private final CityRequestHandler cityRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/sa**" на основе их данных аутентификации и авторизации.
   */
  private final SuperAdminRequestHandler superAdminRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/users**" на основе их данных аутентификации и авторизации.
   */
  private final UserRequestHandler userRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/benefits**" на основе их данных аутентификации и авторизации.
   */
  private final BenefitRequestHandler benefitRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/criteria**" на основе их данных аутентификации и авторизации.
   */
  private final CriterionRequestHandler criterionRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/criterion-types**" на основе их данных аутентификации и авторизации.
   */
  private final CriterionTypeRequestHandler criterionTypeRequestHandler;
  /**
   * Обрабатывает запросы вида "/api/institutions**" на основе их данных аутентификации и авторизации.
   */
  private final InstitutionRequestHandler institutionRequestHandler;

  /**
   * Конструктор для инициализации сервисов
   * @param adminRequestHandler обрабатывает запросы вида "/api/admins" на основе их данных аутентификации и авторизации
   * @param authRequestHandler обрабатывает запросы вида "/api/auth" на основе их данных аутентификации и авторизации
   * @param cityRequestHandler обрабатывает запросы вида "/api/cities" на основе их данных аутентификации и авторизации
   * @param superAdminRequestHandler обрабатывает запросы вида "/api/sa" на основе их данных аутентификации и авторизации
   * @param userRequestHandler обрабатывает запросы вида "/api/users" на основе их данных аутентификации и авторизации
   * @param benefitRequestHandler обрабатывает запросы вида "/api/benefits**" на основе их данных аутентификации и авторизации.
   * @param criterionRequestHandler обрабатывает запросы вида "/api/criteria**" на основе их данных аутентификации и авторизации.
   * @param criterionTypeRequestHandler обрабатывает запросы вида "/api/criterion-types**" на основе их данных аутентификации и авторизации.
   * @param institutionRequestHandler обрабатывает запросы вида "/api/institutions**" на основе их данных аутентификации и авторизации.
   */
  @Autowired
  public AllRequestsFilterFB(AdminRequestHandler adminRequestHandler,
                             AuthRequestHandler authRequestHandler,
                             CityRequestHandler cityRequestHandler,
                             SuperAdminRequestHandler superAdminRequestHandler,
                             UserRequestHandler userRequestHandler,
                             BenefitRequestHandler benefitRequestHandler,
                             CriterionRequestHandler criterionRequestHandler,
                             CriterionTypeRequestHandler criterionTypeRequestHandler,
                             InstitutionRequestHandler institutionRequestHandler) {
    this.adminRequestHandler = adminRequestHandler;
    this.authRequestHandler = authRequestHandler;
    this.cityRequestHandler = cityRequestHandler;
    this.superAdminRequestHandler = superAdminRequestHandler;
    this.userRequestHandler = userRequestHandler;
    this.benefitRequestHandler = benefitRequestHandler;
    this.criterionRequestHandler = criterionRequestHandler;
    this.criterionTypeRequestHandler = criterionTypeRequestHandler;
    this.institutionRequestHandler = institutionRequestHandler;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {

    String requestURI = request.getRequestURI();
    boolean isSuccess = false;

    if (requestURI.startsWith("/api/cities")) {
      isSuccess = cityRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/users")) {
      isSuccess = userRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/admins")) {
      isSuccess = adminRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/auth")){
      isSuccess = authRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/sa")){
      isSuccess = superAdminRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/benefits")){
      isSuccess = benefitRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/criteria")){
      isSuccess = criterionRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/criterion-types")){
      isSuccess = criterionTypeRequestHandler.handle(request, response);
    } else if (requestURI.startsWith("/api/institutions")){
      isSuccess = institutionRequestHandler.handle(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    if (isSuccess) {
      filterChain.doFilter(request, response);
    }
  }
}
