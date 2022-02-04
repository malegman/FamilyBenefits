package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.CriterionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   * Обрабатывает POST запрос "/criteriontype" на добавление нового типа критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param criterionTypeAdd объект запроса для добавления типа критерия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/criteriontype")
  public ResponseEntity<?> addCriterionType(@RequestBody CriterionTypeAdd criterionTypeAdd) {

    if (criterionTypeAdd == null) {
      log.warn("POST \"/criteriontype\": {}", "Request body \"criterionTypeAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionTypeService.add(criterionTypeAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      // Тип критерия с указанным названием существует
      log.error("POST \"/criteriontype\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/criteriontype" на обновление типа критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/criteriontype")
  public ResponseEntity<?> updateCriterionType(@RequestBody CriterionTypeUpdate criterionTypeUpdate) {

    if (criterionTypeUpdate == null) {
      log.warn("PUT \"/criteriontype\": {}", "Request body \"criterionTypeUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionTypeService.update(criterionTypeUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("PUT \"/criteriontype\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/criteriontype/{id}" на удаление типа критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCriterionType ID типа критерия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/criteriontype/{id}")
  public ResponseEntity<?> deleteCriterionType(@PathVariable(name = "id") String idCriterionType) {

    try {
      criterionTypeService.delete(idCriterionType);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("DELETE \"/criteriontype/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criteriontype/{id}" на получение информации о типе критерия.
   * Выполнить запрос может любой клиент
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteriontype/{id}")
  public ResponseEntity<CriterionTypeInfo> getCriterionType(@PathVariable(name = "id") String idCriterionType) {

    try {
      CriterionTypeInfo criterionTypeInfo = criterionTypeService.read(idCriterionType);
      return ResponseEntity.status(HttpStatus.OK).body(criterionTypeInfo);

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("GET \"/criteriontype/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criteriontype/all" на получение множества типов критерия,
   * в которых есть критерии.
   * Выполнить запрос может любой клиент
   * @return множество типов критерий и код ответа
   */
  @GetMapping(value = "/criteriontype/all")
  public ResponseEntity<Set<CriterionTypeInfo>> getCriterionTypes() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionTypeService.getAll());
  }

  /**
   * Обрабатывает GET запрос "/criteriontype/allpartial" на получение множества типов критерия,
   * в которых нет критерий.
   * Выполнить запрос может любой клиент
   * @return множество типов критерий и код ответа
   */
  @GetMapping(value = "/criteriontype/allpartial")
  public ResponseEntity<Set<CriterionTypeInfo>> getPartialCriterionTypes() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionTypeService.getAllPartial());
  }
}
