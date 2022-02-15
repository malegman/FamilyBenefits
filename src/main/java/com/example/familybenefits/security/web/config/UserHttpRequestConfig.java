package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.UserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с пользователем, с путем "/users**"
 */
@Configuration
@EnableWebSecurity
public class UserHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с пользователем, и с путем "/users**"
   */
  private final UserAuthenticationFilter userAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param userAuthenticationFilter фильтр для запросов, связанных с пользователем, и с путем "/users**"
   */
  @Autowired
  public UserHttpRequestConfig(UserAuthenticationFilter userAuthenticationFilter) {
    this.userAuthenticationFilter = userAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с пользователем, и с путем "/users**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.POST, "/users").anonymous()
        .antMatchers(HttpMethod.GET, "/users/{id}").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/users/{id}").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/users/init-data").permitAll()

        .and()

        .addFilter(userAuthenticationFilter);
  }
}
