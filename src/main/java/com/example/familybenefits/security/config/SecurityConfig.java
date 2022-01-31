package com.example.familybenefits.security.config;

import com.example.familybenefits.security.service.implementation.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурирует параметры безопасности при взаимодействии с веб клиентом
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для фильтрации клиентских запросов, с использованием JWT
   */
  private final JwtFilter jwtFilter;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param jwtFilter фильтр для фильтрации клиентских запросов, с использованием JWT
   */
  @Autowired
  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  /**
   * Конфигурирует безопасность по протоколу http
   * @param http объект для конфигурации
   * @throws Exception если конфигурация безопасности настроена некорректно
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()

        // requests: system
        .antMatchers(HttpMethod.POST, "/logout", "/verifemail").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.PUT, "/changepwd", "/verifemail").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.POST, "/login", "/recoverpwd", "/refresh").permitAll()
        .antMatchers(HttpMethod.PUT, "/recoverpwd").permitAll()

        // requests: user
        .antMatchers(HttpMethod.PUT, "/user").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/user/{id}", "/user/{id}/benefits").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/user/{id}").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/user").permitAll()
        .antMatchers(HttpMethod.GET, "/user/initdata").permitAll()

        // requests: admin
        .antMatchers(HttpMethod.POST, "/admin", "/admin/fromuser/{id}", "/admin/{id}/touser", "/admin/{id}/tosuper").hasRole("SUPER_ADMIN")
        .antMatchers(HttpMethod.PUT, "/admin").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/admin/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/admin/{id}").hasRole("SUPER_ADMIN")

        // requests: city
        .antMatchers(HttpMethod.POST, "/city").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/city").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/city/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/city/initdata").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/city/{id}", "/city/all").permitAll()

        // requests: benefit
        .antMatchers(HttpMethod.POST, "/benefit").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/benefit").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/benefit/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefit/initdata", "/benefit/allpartial").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefit/{id}", "/benefit/all").permitAll()

        // requests: institution
        .antMatchers(HttpMethod.POST, "/institution").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/institution").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/institution/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institution/initdata").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institution/{id}", "/institution/all").permitAll()

        // requests: criterion
        .antMatchers(HttpMethod.POST, "/criterion").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/criterion").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criterion/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion/initdata", "/criterion/allpartial").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion/{id}", "/criterion/all").permitAll()

        // requests: criterion type
        .antMatchers(HttpMethod.POST, "/criteriontype").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/criteriontype").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criteriontype/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criteriontype/{id}", "/criteriontype/all").permitAll()

        .and()
        .anonymous().disable()
        .exceptionHandling()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

        .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * Предоставляет объект для хеширования пароля
   * @return объект для хеширования пароля
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
