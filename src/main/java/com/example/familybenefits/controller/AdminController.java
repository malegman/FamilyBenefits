package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Контроллер запросов, звязанных с администратором
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
   * Добавляет нового администратора
   * @param adminAdd объект запроса для добавления администратора
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin")
  public ResponseEntity<?> addAdmin(@RequestBody AdminAdd adminAdd) {

    if (adminAdd == null) {
      log.warn("Request body \"adminAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.add(adminAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (PasswordNotSafetyException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (InvalidEmailException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (PasswordNotEqualsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет данные администратора
   * @param adminUpdate объект запроса для обновления администратора
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/admin")
  public ResponseEntity<?> updateAdmin(@RequestBody AdminUpdate adminUpdate) {

    if (adminUpdate == null) {
      log.warn("Request body \"adminUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      adminService.update(adminUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (InvalidEmailException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Возвращает информацию об администраторе
   * @param idAdmin ID администратора
   * @return информация об администраторе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/admin/{id}")
  public ResponseEntity<AdminInfo> getAdmin(@PathVariable(name = "id") BigInteger idAdmin) {

    try {
      AdminInfo adminInfo = adminService.read(idAdmin);
      return ResponseEntity.status(HttpStatus.OK).body(adminInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет администратора
   * @param idAdmin ID администратора
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/admin/{id}")
  public ResponseEntity<?> deleteAdmin(@PathVariable(name = "id")BigInteger idAdmin) {

    try {
      adminService.delete(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Добавляет роль администратора пользователю
   * @param idUser ID пользователя, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin/fromuser/{id}")
  public ResponseEntity<?> adminFromUser(@PathVariable(name = "id")BigInteger idUser) {

    try {
      adminService.fromUser(idUser);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Добавляет роль пользователя администратору
   * @param idAdmin ID администратора, которому добавляется роль
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/admin/touser/{id}")
  public ResponseEntity<?> adminToUser(@PathVariable(name = "id")BigInteger idAdmin) {

    try {
      adminService.toUser(idAdmin);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
