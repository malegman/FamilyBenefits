package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserSave;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import com.example.familybenefits.service.s_interface.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер запросов, связанных с пользователем
 */
@Slf4j
@RestController
public class UserController {

  /**
   * Интерфейс сервиса, управляющего объектом "пользователь"
   */
  private final UserService userService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param userService интерфейс сервиса, управляющего объектом "пользователь"
   */
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Обрабатывает POST запрос "/users" на создание пользователя. Регистрация гостя
   * Для незарегистрированного клиента.
   * @param userSave объект запроса для сохранения пользователя
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/users")
  public ResponseEntity<?> create(@RequestBody UserSave userSave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если тело запроса пустое
    if (userSave == null) {
      log.warn("{} POST \"/users\": Request body \"userSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      userService.create(userSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены критерии или город
      log.error("{} POST \"/users\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException
        | InvalidEmailException
        | DateTimeException
        | DateFormatException e) {
      // Администратор или пользователь с указанным email существует.
      // Строка в поле "email" не является email.
      // Даты позже текущей даты.
      // Даты не соответствуют формату "dd.mm.yyyy".
      log.error("{} POST \"/users\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/users/{id}" на получение информации о пользователе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return информация о пользователе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/users/{id}")
  public ResponseEntity<UserInfo> read(@PathVariable(name = "id") String idUser,
                                       @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если пользователь пытается получить информацию не о своем профиле
    if (!userAuth.getIdUser().equals(idUser)) {
      log.warn("{} GET \"/admins/{id}\": User with id {} tried to read user with id {}", userIp, userAuth.getIdUser(), idUser);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
      UserInfo userInfo = userService.read(idUser);
      return ResponseEntity.status(HttpStatus.OK).body(userInfo);

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("{} GET \"/users/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/users/{id}" на обновление пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @param userSave объект запроса для сохранения пользователя
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/users/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idUser,
                                  @RequestBody UserSave userSave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если пользователь пытается обновить не свой профиль
    if (!userAuth.getIdUser().equals(idUser)) {
      log.warn("{} PUT \"/admins/{id}\": User with id {} tried to update user with id {}", userIp, userAuth.getIdUser(), idUser);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Если тело запроса пустое
    if (userSave == null) {
      log.warn("{} PUT \"/users/{id}\": Request body \"userSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      userService.update(idUser, userSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь или не найдены критерии или город
      log.error("{} PUT \"/users/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (InvalidEmailException
        | DateTimeException
        | DateFormatException
        | AlreadyExistsException e) {
      // Строка в поле "email" не является email.
      // Даты позже текущей даты.
      // Даты не соответствуют формату "dd.mm.yyyy".
      // Пользователь с отличным ID и данным email уже существует.
      log.error("{} PUT \"/users/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/users/{id}" на удаление пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/users/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idUser,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если пользователь пытается удалить не свой профиль
    if (!userAuth.getIdUser().equals(idUser)) {
      log.warn("{} PUT \"/admins/{id}\": User with id {} tried to delete user with id {}", userIp, userAuth.getIdUser(), idUser);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
      userService.delete(idUser);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("{} DELETE \"/users/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/users/init-data" на получение дополнительных данных для пользователя.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях.
   * Выполнить запрос может любой клиент
   * @return дополнительные данные для пользователя и код ответа
   */
  @GetMapping(value = "/users/init-data")
  public ResponseEntity<UserInitData> getInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.getInitData());
  }
}
