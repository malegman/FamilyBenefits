package com.example.familybenefits.security.config;

import com.example.familybenefits.security.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурирует параметры безопасности при взаимодействии с веб клиентом
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Фильтр для фильрации клиентских запросов, с использованием JWT
   */
  private final JwtFilter jwtFilter;

  /**
   * Консутруктор для инициализации интерфейса сервиса
   * @param jwtFilter фильтр для фильрации клиентских запросов, с использованием JWT
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

        // requests: service
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
        .antMatchers(HttpMethod.POST, "/admin").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/admin").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/admin/{id}", "/admin/initdata").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/admin/{id}").hasRole("ADMIN")

        // requests: city
        .antMatchers(HttpMethod.POST, "/cityEntity").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/cityEntity").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/cityEntity/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/cityEntity/{id}", "/cityEntity/{id}/*", "/cityEntity/all").permitAll()

        // requests: benefit
        .antMatchers(HttpMethod.POST, "/benefit").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/benefit").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/benefit/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefit/initdata", "/benefit/allex").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/benefit/{id}", "/benefit/{id}/*").permitAll()

        // requests: institution
        .antMatchers(HttpMethod.POST, "/institution").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/institution").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/institution/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institution/initdata").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/institution/{id}", "/institution/{id}/*").permitAll()

        // requests: criterion
        .antMatchers(HttpMethod.POST, "/criterion").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/criterion").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criterion/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion/initdata", "/criterion/allex").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criterion/{id}", "/criterion/{id}/*", "/criterion/all").permitAll()

        // requests: criterion type
        .antMatchers(HttpMethod.POST, "/criteriontype").hasRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/criteriontype").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/criteriontype/{id}").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/criteriontype/{id}", "/criteriontype/{id}/*", "/criteriontype/all").permitAll()

        .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * Предоставляет объект для хэширования пароля
   * @return объект для хэширования пароля
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
