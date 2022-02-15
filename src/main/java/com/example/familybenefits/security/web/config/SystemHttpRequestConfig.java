package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.SystemAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с системой, входом / выходом
 */
@Configuration
@EnableWebSecurity
public class SystemHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с системой, входом / выходом
   */
  private final SystemAuthenticationFilter systemAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param systemAuthenticationFilter фильтр для запросов, связанных с системой, входом / выходом
   */
  @Autowired
  public SystemHttpRequestConfig(SystemAuthenticationFilter systemAuthenticationFilter) {
    this.systemAuthenticationFilter = systemAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с системой, входом / выходом
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.POST, "/logout").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.POST, "/login", "/pre-login").anonymous()

        .and()

        .addFilter(systemAuthenticationFilter);
  }
}
