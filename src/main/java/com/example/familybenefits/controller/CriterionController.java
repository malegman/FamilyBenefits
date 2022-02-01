package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.CriterionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Set;

/**
 * Контроллер запросов, связанных с критерием
 */
@Slf4j
@RestController
public class CriterionController {

  /**
   * Интерфейс сервиса, управляющего объектом "критерий"
   */
  private final CriterionService criterionService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param criterionService интерфейс сервиса, управляющего объектом "критерий"
   */
  @Autowired
  public CriterionController(CriterionService criterionService) {
    this.criterionService = criterionService;
  }

  /**
   * Обрабатывает POST запрос "/criterion" на добавление нового критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param criterionAdd объект запроса для добавления критерия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/criterion")
  public ResponseEntity<?> addCriterion(@RequestBody CriterionAdd criterionAdd) {

    if (criterionAdd == null) {
      log.warn("POST \"/criterion\": {}", "Request body \"criterionAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionService.add(criterionAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("POST \"/criterion\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Критерий с указанным названием существует
      log.error("POST \"/criterion\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/criterion" на обновление критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param criterionUpdate объект запроса для обновления критерия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/criterion")
  public ResponseEntity<?> updateCriterion(@RequestBody CriterionUpdate criterionUpdate) {

    if (criterionUpdate == null) {
      log.warn("PUT \"/criterion\": {}", "Request body \"criterionUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionService.update(criterionUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден критерий или тип критерия
      log.error("PUT \"/criterion\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/criterion/{id}" на удаление критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCriterion ID критерия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/criterion/{id}")
  public ResponseEntity<?> deleteCriterion(@PathVariable(name = "id")BigInteger idCriterion) {

    try {
      criterionService.delete(idCriterion);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден критерий
      log.error("DELETE \"/criterion/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criterion/{id}" на получение информации о критерии.
   * Выполнить запрос может любой клиент
   * @param idCriterion ID критерия
   * @return информация о критерии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criterion/{id}")
  public ResponseEntity<CriterionInfo> getCriterion(@PathVariable(name = "id") BigInteger idCriterion) {

    try {
      CriterionInfo criterionInfo = criterionService.read(idCriterion);
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfo);

    } catch (NotFoundException e) {
      // Не найден критерий
      log.error("GET \"/criterion/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criterion/all" на получение множества критерий,
   * в которых есть пособия.
   * Выполнить запрос может любой клиент
   * @return множество критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criterion/all")
  public ResponseEntity<Set<CriterionInfo>> getCriteria() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionService.getAll());
  }

  /**
   * Обрабатывает GET запрос "/criterion/allpartial" на получение множества критерий,
   * в которых нет пособий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criterion/allpartial")
  public ResponseEntity<Set<CriterionInfo>> getPartialCriteria() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionService.getAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/criterion/initdata" на получение дополнительных данных для критерия.
   * Данные содержат в себе множество кратких информаций о типах критерий
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criterion/initdata")
  public ResponseEntity<CriterionInitData> getCriterionInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionService.getInitData());
  }
}
