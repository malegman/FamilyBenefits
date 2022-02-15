package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.CriterionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Контроллер запросов, связанных с типом критерия
 */
@Slf4j
@RestController
public class CriterionTypeController {

  /**
   * Интерфейс сервиса, управляющего объектом "тип критерия"
   */
  private final CriterionTypeService criterionTypeService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param criterionTypeService интерфейс сервиса, управляющего объектом "тип критерия"
   */
  @Autowired
  public CriterionTypeController(CriterionTypeService criterionTypeService) {
    this.criterionTypeService = criterionTypeService;
  }

  /**
   * Обрабатывает GET запрос "/criterion-types/all" на получение множества типов критерия,
   * в которых есть критерии.
   * Выполнить запрос может любой клиент
   * @return множество типов критерий и код ответа
   */
  @GetMapping(
      value = "/criterion-types",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  @ResponseBody
  public ResponseEntity<Set<ObjectShortInfo>> readAll() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionTypeService.readAll());
  }

  /**
   * Обрабатывает POST запрос "/criterion-types" на создание типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @param request http запрос
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(
      value = "/criterion-types",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<?> create(@RequestBody CriterionTypeSave criterionTypeSave,
                                  HttpServletRequest request) {

    String userIp = request.getRemoteAddr();


    // Если тело запроса пустое
    if (criterionTypeSave == null) {
      log.warn("{} POST \"/criterion-types\": Request body \"criterionTypeSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionTypeService.create(criterionTypeSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      // Тип критерия с указанным названием существует
      log.error("{} POST \"/criterion-types\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criterion-types/{id}" на получение информации о типе критерия.
   * Выполнить запрос может любой клиент
   * @param idCriterionType ID типа критерия
   * @param request http запрос
   * @return информация о типе критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/criterion-types/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  @ResponseBody
  public ResponseEntity<CriterionTypeInfo> read(@PathVariable(name = "id") String idCriterionType,
                                                HttpServletRequest request) {

    String userIp = request.getRemoteAddr();

    try {
      CriterionTypeInfo criterionTypeInfo = criterionTypeService.read(idCriterionType);
      return ResponseEntity.status(HttpStatus.OK).body(criterionTypeInfo);

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("{} GET \"/criterion-types/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/criterion-types/{id}" на обновление типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterionType ID типа критерия
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @param request http запрос
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(
      value = "/criterion-types/{id}",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCriterionType,
                                  @RequestBody CriterionTypeSave criterionTypeSave,
                                  HttpServletRequest request) {

    String userIp = request.getRemoteAddr();


    // Если тело запроса пустое
    if (criterionTypeSave == null) {
      log.warn("{} PUT \"/criterion-types/{id}\": Request body \"criterionTypeSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionTypeService.update(idCriterionType, criterionTypeSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("{} PUT \"/criterion-types/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Тип критерия с отличным ID и данным названием уже существует
      log.error("{} PUT \"/criterion-types/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/criterion-types/{id}" на удаление типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterionType ID типа критерия
   * @param request http запрос
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(
      value = "/criterion-types/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCriterionType,
                                  HttpServletRequest request) {

    String userIp = request.getRemoteAddr();

    try {
      criterionTypeService.delete(idCriterionType);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("{} DELETE \"/criterion-types/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criterion-types/partial" на получение множества типов критерия,
   * в которых нет критерий.
   * Выполнить запрос может любой клиент
   * @return множество типов критерий и код ответа
   */
  @GetMapping(
      value = "/criterion-types/partial",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  @ResponseBody
  public ResponseEntity<Set<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionTypeService.readAllPartial());
  }
}
