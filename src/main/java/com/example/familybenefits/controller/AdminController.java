package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.admin.AdminSave;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.service.s_interface.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   * Обрабатывает POST запрос "/admins" на создание администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param adminSave объект запроса для сохранения администратора
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admins")
  public ResponseEntity<?> create(@RequestBody AdminSave adminSave) {

    if (adminSave == null) {
      log.warn("POST \"/admins\": {}", "Request body \"adminSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.create(adminSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException | InvalidEmailException e) {
      // Администратор или пользователь с указанным email существует.
      // Строка в поле "email" не является email.
      log.error("POST \"/admins\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/admins/{id}" на получение информации об администраторе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idAdmin ID администратора
   * @return информация об администраторе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/admins/{id}")
  public ResponseEntity<AdminInfo> read(@PathVariable(name = "id") String idAdmin) {

    try {
      AdminInfo adminInfo = adminService.read(idAdmin);
      return ResponseEntity.status(HttpStatus.OK).body(adminInfo);

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("GET \"/admins/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/admins/{id}" на обновление администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idAdmin ID администратора
   * @param adminSave объект запроса для сохранения администратора
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/admins/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idAdmin,
                                  @RequestBody AdminSave adminSave) {

    if (adminSave == null) {
      log.warn("PUT \"/admins/{id}\": {}", "Request body \"adminSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.update(idAdmin, adminSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("PUT \"/admins/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (InvalidEmailException | AlreadyExistsException e) {
      // Строка в поле "email" не является email.
      // Администратор или пользователь с отличным ID и данным email уже существует
      log.error("PUT \"/admins/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/admins/{id}" на удаление администратора.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/admins/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idAdmin) {

    try {
      adminService.delete(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("DELETE \"/admins/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Администратор имеет роль "ROLE_SUPER_ADMIN"
      log.error("DELETE \"/admins/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admins/from-user/{id}" на добавление роли администратора пользователю.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idUser ID пользователя, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admins/from-user/{id}")
  public ResponseEntity<?> fromUser(@PathVariable(name = "id") String idUser) {

    try {
      adminService.fromUser(idUser);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("POST \"/admins/from-user/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Пользователь имеет роль "ROLE_ADMIN" или не имеет роль "ROLE_USER"
      log.error("POST \"/admins/from-user/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admins/{id}/to-user" на добавление роли пользователя администратору.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admins/{id}/to-user")
  public ResponseEntity<?> toUser(@PathVariable(name = "id") String idAdmin) {

    try {
      adminService.toUser(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден администратор
      log.error("POST \"/admins/{id}/to-user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Пользователь имеет роль "ROLE_USER" или не имеет роль "ROLE_ADMIN"
      log.error("POST \"/admins/{id}/to-user\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает POST запрос "/admins/{id}/to-super" на передачу роли "супер администратора".
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_SUPER_ADMIN"
   * @param idAdmin ID администратора, которому передается роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admins/{id}/to-super")
  public ResponseEntity<?> toSuper(@PathVariable(name = "id") String idAdmin) {

    try {
      adminService.toSuper(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("POST \"/admins/{id}/to-super\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (UserRoleException e) {
      // Администратор имеет роль "ROLE_SUPER_ADMIN"
      log.error("POST \"/admins/{id}/to-super\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
