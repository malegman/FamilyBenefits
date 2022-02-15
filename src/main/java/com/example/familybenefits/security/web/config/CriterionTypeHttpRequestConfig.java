package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.CriterionTypeAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с типом критерия, с путем "/criterion-types**"
 */
@Configuration
@EnableWebSecurity
public class CriterionTypeHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с типом критерия, с путем "/criterion-types**"
   */
  private final CriterionTypeAuthenticationFilter criterionTypeAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param criterionTypeAuthenticationFilter фильтр для запросов, связанных с типом критерия, с путем "/criterion-types**"
   */
  @Autowired
  public CriterionTypeHttpRequestConfig(CriterionTypeAuthenticationFilter criterionTypeAuthenticationFilter) {
    this.criterionTypeAuthenticationFilter = criterionTypeAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с типом критерия, с путем "/criterion-types**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/criterion-types").permitAll()
        .antMatchers(HttpMethod.POST, "/criterion-types").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion-types/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/criterion-types/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criterion-types/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion-types/partial").hasRole("ADMIN")

        .and()

        .addFilter(criterionTypeAuthenticationFilter);
  }
}
