package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.service.s_interface.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Контроллер запросов, связанных с администратором
 */
@Slf4j
@RestController
public class AdminController {

  /**
   * Интерфейс сервиса, управляющего объектом "администратор"
   */
  private final AdminService adminService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param adminService интерфейс сервиса, управляющего объектом "администратор"
   */
  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  /**
   * Обрабатывает POST запрос "/admin" на добавление нового администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param adminAdd объект запроса для добавления администратора
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin")
  public ResponseEntity<?> addAdmin(@RequestBody AdminAdd adminAdd) {

    if (adminAdd == null) {
      log.warn("POST \"/admin\": {}", "Request body \"adminAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.add(adminAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException | PasswordNotSafetyException | InvalidEmailException | PasswordNotEqualsException e) {
      // Администратор или пользователь с указанным email существует.
      // Пароль небезопасный.
      // Строка в поле "email" не является email.
      // Пароли не совпадают.
      log.error("POST \"/admin\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/admin" на обновление администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param adminUpdate объект запроса для обновления администратора
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/admin")
  public ResponseEntity<?> updateAdmin(@RequestBody AdminUpdate adminUpdate) {

    if (adminUpdate == null) {
      log.warn("PUT \"/admin\": {}", "Request body \"adminUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.update(adminUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("PUT \"/admin\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (InvalidEmailException e) {
      // Строка в поле "email" не является email
      log.error("PUT \"/admin\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/admin/{id}" на удаление администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/admin/{id}")
  public ResponseEntity<?> deleteAdmin(@PathVariable(name = "id")BigInteger idAdmin) {

    try {
      adminService.delete(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("DELETE \"/admin/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Администратор имеет роль "ROLE_SUPER_ADMIN"
      log.error("DELETE \"/admin/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/admin/{id}" на получение информации об администраторе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idAdmin ID администратора
   * @return информация об администраторе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/admin/{id}")
  public ResponseEntity<AdminInfo> getAdmin(@PathVariable(name = "id") BigInteger idAdmin) {

    try {
      AdminInfo adminInfo = adminService.read(idAdmin);
      return ResponseEntity.status(HttpStatus.OK).body(adminInfo);

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("GET \"/admin/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admin/fromuser/{id}" на добавление роли администратора пользователю.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idUser ID пользователя, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin/fromuser/{id}")
  public ResponseEntity<?> adminFromUser(@PathVariable(name = "id")BigInteger idUser) {

    try {
      adminService.fromUser(idUser);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("POST \"/admin/fromuser/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Пользователь имеет роль "ROLE_ADMIN" или не имеет роль "ROLE_USER"
      log.error("POST \"/admin/fromuser/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admin/{id}/touser" на добавление роли пользователя администратору.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin/{id}/touser")
  public ResponseEntity<?> adminToUser(@PathVariable(name = "id")BigInteger idAdmin) {

    try {
      adminService.toUser(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("POST \"/admin/{id}/touser\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Пользователь имеет роль "ROLE_USER" или не имеет роль "ROLE_ADMIN"
      log.error("POST \"/admin/{id}/touser\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admin/{id}/tosuper" на передачу роли "супер администратора".
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора, которому передается роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin/{id}/tosuper")
  public ResponseEntity<?> adminToSuper(@PathVariable(name = "id")BigInteger idAdmin) {

    try {
      adminService.toSuper(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("POST \"/admin/{id}/tosuper\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Администратор имеет роль "ROLE_SUPER_ADMIN"
      log.error("POST \"/admin/{id}/tosuper\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
