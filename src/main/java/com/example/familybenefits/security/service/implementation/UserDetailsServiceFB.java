package com.example.familybenefits.security.service.implementation;

import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.dao.repository.UserRepository;
import com.example.familybenefits.security.model.UserDetailsFB;
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
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
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

    // Получение пользователя по его email, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(
            "User with username (email) %s not found", username)));

    return UserDetailsFB.fromUserEntity(userEntityFromRequest);
  }
}
