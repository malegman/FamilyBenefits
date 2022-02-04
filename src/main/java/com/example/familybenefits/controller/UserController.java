package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserUpdate;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.service.s_interface.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
   * Обрабатывает POST запрос "/user" на добавление нового пользователя. Регистрация гостя
   * Для незарегистрированного клиента.
   * @param userAdd объект запроса для добавления пользователя
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/user")
  public ResponseEntity<?> addUser(@RequestBody UserAdd userAdd) {

    if (userAdd == null) {
      log.warn("POST \"/user\": {}", "Request body \"userAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      userService.add(userAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены критерии или город
      log.error("POST \"/user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException
        | InvalidEmailException
        | PasswordNotSafetyException
        | NotEqualException
        | DateTimeException
        | DateFormatException e) {
      // Администратор или пользователь с указанным email существует.
      // Строка в поле "email" не является email.
      // Пароль небезопасный.
      // Пароли не совпадают.
      // Даты позже текущей даты.
      // Даты не соответствуют формату "dd.mm.yyyy".
      log.error("POST \"/user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/user" на обновление пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param userUpdate объект запроса для обновления пользователя
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/user")
  public ResponseEntity<?> updateUser(@RequestBody UserUpdate userUpdate) {

    if (userUpdate == null) {
      log.warn("PUT \"/user\": {}", "Request body \"userUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      userService.update(userUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь или не найдены критерии или город
      log.error("PUT \"/user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (InvalidEmailException
        | DateTimeException
        | DateFormatException e) {
      // Строка в поле "email" не является email.
      // Даты позже текущей даты.
      // Даты не соответствуют формату "dd.mm.yyyy".
      log.error("PUT \"/user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/user/{id}" на удаление пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/user/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable(name = "id") String idUser) {

    try {
      userService.delete(idUser);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("DELETE \"/user/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/user/{id}" на получение информации о пользователе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @return информация о пользователе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/user/{id}")
  public ResponseEntity<UserInfo> getUser(@PathVariable(name = "id") String idUser) {

    try {
      UserInfo userInfo = userService.read(idUser);
      return ResponseEntity.status(HttpStatus.OK).body(userInfo);

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("GET \"/user/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/user/{id}/benefits" на получение множества предложенных пособий пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @return множество предложенных пособий пользователя и код ответа
   */
  @GetMapping(value = "/user/{id}/benefits")
  public ResponseEntity<Set<BenefitInfo>> getUserBenefits(@PathVariable(name = "id") String idUser) {

    try {
      Set<BenefitInfo> benefitInfoSet = userService.getBenefits(idUser);
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfoSet);

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("GET \"/user/{id}/benefits\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (DateTimeException e) {
      // Критерии пользователя устарели
      log.error("GET \"/user/{id}/benefits\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/user/initdata" на получение дополнительных данных для пользователя.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях.
   * Выполнить запрос может любой клиент
   * @return дополнительные данные для пользователя и код ответа
   */
  @GetMapping(value = "/user/initdata")
  public ResponseEntity<UserInitData> getUserInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.getInitData());
  }
}
