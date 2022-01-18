package com.example.familybenefits.security.service;

import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.security.model.UserDetailsFB;
import com.example.familybenefits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с объектом пользователя для авторизации
 */
@Service
public class UserDetailsServiceFB implements UserDetailsService {

  /**
   * Сервис для взаимодействия с пользователем
   */
  private final UserService userService;

  /**
   * Конструктор для иниуиализации интерфейса сервиса
   * @param userService сервис для взаимодействия с пользователем
   */
  @Autowired
  public UserDetailsServiceFB(UserService userService) {
    this.userService = userService;
  }

  /**
   * Возвращает объект пользователя для авторизации по его имени
   * @param username имя пользователя. В данной реализации - email
   * @return Объект пользователя для авторизации
   * @throws UsernameNotFoundException если объект с указанным именем (email) не найден
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserEntity userEntity = userService.getByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(String.format("Объект с email %s не найден", username));
    }

    return UserDetailsFB.fromUserEntity(userEntity);
  }
}
