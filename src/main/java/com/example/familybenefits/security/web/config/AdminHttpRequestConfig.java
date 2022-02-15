package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.AdminAuthenticationFilterFB;
import com.example.familybenefits.security.web.filter.UserAuthenticationFilterFB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с администратором, с путем "/admins**"
 */
@Configuration
@EnableWebSecurity
public class AdminHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с пользователем, и с путем "/admins**"
   */
  private final AdminAuthenticationFilterFB adminAuthenticationFilterFB;

  /**
   * Конструктор для инициализации фильтра
   * @param adminAuthenticationFilterFB фильтр для запросов, связанных с администратором, и с путем "/admins**"
   */
  @Autowired
  public AdminHttpRequestConfig(AdminAuthenticationFilterFB adminAuthenticationFilterFB) {
    this.adminAuthenticationFilterFB = adminAuthenticationFilterFB;
  }

  /**
   * Конфигурирует конечные точки, связанные с администратором, с путем "/admins**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.POST, "/admins", "/admins/from-user/{id}", "/admins/{id}/to-user", "/admins/{id}/to-super").hasRole("SUPER_ADMIN")
        .antMatchers(HttpMethod.GET, "/admins/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/admins/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/admins/{id}").hasRole("SUPER_ADMIN")

        .and()

        .addFilter(adminAuthenticationFilterFB);
  }
}
