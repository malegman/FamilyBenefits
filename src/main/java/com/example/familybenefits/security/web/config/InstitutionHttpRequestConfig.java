package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.CityAuthenticationFilterFB;
import com.example.familybenefits.security.web.filter.InstitutionAuthenticationFilterFB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с учреждением, с путем "/institutions**"
 */
@Configuration
@EnableWebSecurity
public class InstitutionHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с учреждением, и с путем "/institutions**"
   */
  private final InstitutionAuthenticationFilterFB institutionAuthenticationFilterFB;

  /**
   * Конструктор для инициализации фильтра
   * @param institutionAuthenticationFilterFB фильтр для запросов, связанных с учреждением, и с путем "/institutions**"
   */
  @Autowired
  public InstitutionHttpRequestConfig(InstitutionAuthenticationFilterFB institutionAuthenticationFilterFB) {
    this.institutionAuthenticationFilterFB = institutionAuthenticationFilterFB;
  }

  /**
   * Конфигурирует конечные точки, связанные с учреждением, и с путем "/institutions**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/institutions").permitAll()
        .antMatchers(HttpMethod.POST, "/institutions").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institutions/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/institutions/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/institutions/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institutions/partial", "/institutions/init-data").hasRole("ADMIN")

        .and()

        .addFilter(institutionAuthenticationFilterFB);
  }
}
