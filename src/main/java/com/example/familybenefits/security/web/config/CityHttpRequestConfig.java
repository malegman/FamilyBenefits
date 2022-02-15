package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.CityAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с гордом, с путем "/cities**"
 */
@Configuration
@EnableWebSecurity
public class CityHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с гордом, и с путем "/cities**"
   */
  private final CityAuthenticationFilter cityAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param cityAuthenticationFilter фильтр для запросов, связанных с городом, и с путем "/cities**"
   */
  @Autowired
  public CityHttpRequestConfig(CityAuthenticationFilter cityAuthenticationFilter) {
    this.cityAuthenticationFilter = cityAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с гордом, и с путем "/cities**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/cities").permitAll()
        .antMatchers(HttpMethod.POST, "/cities").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/cities/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/cities/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/cities/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/cities/partial").hasRole("ADMIN")

        .and()

        .addFilter(cityAuthenticationFilter);
  }
}
