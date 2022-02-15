package com.example.familybenefits.security.web.config;

import com.example.familybenefits.security.web.filter.BenefitAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация конечных точек, связанных с пособием, с путем "/benefits**"
 */
@Configuration
@EnableWebSecurity
public class BenefitHttpRequestConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для запросов, связанных с пособием, и с путем "/benefits**"
   */
  private final BenefitAuthenticationFilter benefitAuthenticationFilter;

  /**
   * Конструктор для инициализации фильтра
   * @param benefitAuthenticationFilter фильтр для запросов, связанных с пособием, и с путем "/benefits**"
   */
  @Autowired
  public BenefitHttpRequestConfig(BenefitAuthenticationFilter benefitAuthenticationFilter) {
    this.benefitAuthenticationFilter = benefitAuthenticationFilter;
  }

  /**
   * Конфигурирует конечные точки, связанные с пособием, и с путем "/benefits**"
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()

        .antMatchers(HttpMethod.GET, "/benefits").permitAll()
        .antMatchers(HttpMethod.POST, "/benefits").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefits/{id}").permitAll()
        .antMatchers(HttpMethod.PUT, "/benefits/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/benefits/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefits/partial", "/benefits/init-data").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefits/user/{idUser}").hasRole("USER")


        .and()

        .addFilter(benefitAuthenticationFilter);
  }
}
