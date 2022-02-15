package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.CriterionAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с критерием, с путем "/criteria**"
 */
@Configuration
@EnableWebSecurity
public class CriterionHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с критерием, и с путем "/criteria**"
   */
  private final CriterionAuthenticationFilter criterionAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param criterionAuthenticationFilter фильтр для запросов, связанных с критерием, и с путем "/criteria**"
   */
  @Autowired
  public CriterionHttpRequestConfig(CriterionAuthenticationFilter criterionAuthenticationFilter) {
    this.criterionAuthenticationFilter = criterionAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с критерием, и с путем "/criteria**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/criteria").permitAll()
        .antMatchers(HttpMethod.POST, "/criteria").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criteria/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/criteria/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criteria/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criteria/partial", "/criteria/init-data").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criteria/user/{idUser}").hasRole("USER")

        .and()

        .addFilter(criterionAuthenticationFilter);
  }
}
