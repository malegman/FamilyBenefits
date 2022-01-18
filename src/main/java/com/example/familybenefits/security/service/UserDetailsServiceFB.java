package com.example.familybenefits.security.service;

import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.dao.repository.UserRepository;
import com.example.familybenefits.security.model.UserDetailsFB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация сервиса для работы с объектом пользователя для авторизации
 */
@Service
public class UserDetailsServiceFB implements UserDetailsService {

  /**
   * Сервис для взаимодействия с пользователем
   */
  private final UserRepository userRepository;

  /**
   * Конструктор для иниуиализации интерфейса сервиса
   * @param userRepository сервис для взаимодействия с пользователем
   */
  @Autowired
  public UserDetailsServiceFB(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Возвращает объект пользователя для авторизации по его имени
   * @param username имя пользователя. В данной реализации - email
   * @return Объект пользователя для авторизации
   * @throws UsernameNotFoundException если объект с указанным именем (email) не найден
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<UserEntity> optUserEntity = userRepository.findByEmail(username);
    if (optUserEntity.isEmpty()) {
      throw new UsernameNotFoundException(String.format(
          "Объект с email %s не найден", username
      ));
    }

    return UserDetailsFB.fromUserEntity(optUserEntity.get());
  }
}
