package com.example.familybenefits.security.model;

import com.example.familybenefits.dao.entity.UserEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация объекта пользователя, которая будет использоваться в фильтрации http запросов
 */
@Builder
@RequiredArgsConstructor
public class UserDetailsFB implements UserDetails {

  /**
   * Имя пользователя. В качестве имени используется email реального пользователя
   */
  @NonNull
  private String username;

  /**
   * Пароль пользователя
   */
  @NonNull
  private String password;

  /**
   * Права, роли пользователя
   */
  @NonNull
  private Collection<? extends GrantedAuthority> grantedAuthorities;

  /**
   * Получает данный объект пользователя на основе модели пользователя по таблице "user"
   * @param userEntity Модель пользователя, таблицы "user"
   * @return Объект пользователя
   */
  @NonNull
  public static UserDetailsFB fromUserEntity(@NonNull UserEntity userEntity) {

    return UserDetailsFB
        .builder()
        .username(userEntity.getEmail())
        .password(userEntity.getPassword())
        .grantedAuthorities(userEntity.getRoleEntitySet()
                                .stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toSet()))
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  @NotNull
  public String getPassword() {
    return password;
  }

  @Override
  @NotNull
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
