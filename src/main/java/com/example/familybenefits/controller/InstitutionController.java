package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.InstitutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, связанных с учреждением
 */
@Slf4j
@RestController
public class InstitutionController {

  /**
   * Интерфейс сервиса, управляющего объектом "учреждение"
   */
  private final InstitutionService institutionService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param institutionService интерфейс сервиса, управляющего объектом "учреждение"
   */
  @Autowired
  public InstitutionController(InstitutionService institutionService) {
    this.institutionService = institutionService;
  }

  /**
   * Обрабатывает POST запрос "/institution" на добавление нового учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param institutionAdd объект запроса для добавления учреждения
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/institution")
  public ResponseEntity<?> addInstitution(@RequestBody InstitutionAdd institutionAdd) {

    if (institutionAdd == null) {
      log.warn("POST \"/institution\": {}", "Request body \"institutionAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      institutionService.add(institutionAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены города или пособия
      log.error("POST \"/institution\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Учреждение с указанным названием существует
      log.error("POST \"/institution\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/institution" на обновление учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param institutionUpdate объект запроса для обновления учреждения
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/institution")
  public ResponseEntity<?> updateInstitution(@RequestBody InstitutionUpdate institutionUpdate) {

    if (institutionUpdate == null) {
      log.warn("PUT \"/institution\": {}", "Request body \"institutionUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      institutionService.update(institutionUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("PUT \"/institution\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/institution/{id}" на удаление учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idInstitution ID учреждения
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/institution/{id}")
  public ResponseEntity<?> deleteInstitution(@PathVariable(name = "id")BigInteger idInstitution) {

    try {
      institutionService.delete(idInstitution);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("DELETE \"/institution/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/institution/{id}" на получение информации об учреждении.
   * Выполнить запрос может любой клиент
   * @param idInstitution ID учреждения
   * @return информация об учреждении, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/institution/{id}")
  public ResponseEntity<InstitutionInfo> getInstitution(@PathVariable(name = "id")BigInteger idInstitution) {

    try {
      InstitutionInfo institutionInfo = institutionService.read(idInstitution);
      return ResponseEntity.status(HttpStatus.OK).body(institutionInfo);

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("GET \"/institution/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/institution/{id}" на получение множества всех учреждений.
   * Выполнить запрос может любой клиент
   * @return множество учреждений и код ответа
   */
  @GetMapping(value = "/institution/all")
  public ResponseEntity<Set<InstitutionInfo>> getInstitutions() {

    try {
      Set<InstitutionInfo> institutionInfoSet = institutionService.readAll();
      return ResponseEntity.status(HttpStatus.OK).body(institutionInfoSet);

    } catch (NotFoundException e) {
      // Не найдены учреждения
      log.error("GET \"/institution/all\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Обрабатывает GET запрос "/institution/{id}" на получение дополнительных данных для учреждения.
   * Данные содержат в себе множество кратких информаций о городах и пособиях
   * @return дополнительные данные для учреждения и код ответа
   */
  @GetMapping(value = "/institution/initdata")
  public ResponseEntity<InstitutionInitData> getInstitutionInitData() {

    try {
      InstitutionInitData institutionInitData = institutionService.getInitData();
      return ResponseEntity.status(HttpStatus.OK).body(institutionInitData);

    } catch (NotFoundException e) {
      // Не найдены города или пособия
      log.error("GET \"/institution/initdata\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
