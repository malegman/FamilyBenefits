package com.example.familybenefits.part_res_rest_api.controllers;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер запросов, связанных с типом критерия
 */
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
   * Обрабатывает GET запрос "/api/criterion-types/all" на получение списка типов критерия,
   * в которых есть критерии.
   * Фильтр по названию или ID критерия.
   * Выполнить запрос может любой клиент
   * @param name название типа критерия
   * @param idCriterion ID критерия
   * @return список типов критерий и код ответа
   */
  @GetMapping(
      value = "/api/criterion-types",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name,
                                                             @RequestParam(name = "idCriterion", required = false) String idCriterion) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionTypeService.readAllFilter(name, idCriterion));
  }

  /**
   * Обрабатывает POST запрос "/api/criterion-types" на создание типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PostMapping(
      value = "/api/criterion-types",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody CriterionTypeSave criterionTypeSave)
      throws AlreadyExistsException, InvalidStringException {

    criterionTypeService.create(criterionTypeSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/criterion-types/{id}" на получение информации о типе критерия.
   * Выполнить запрос может любой клиент
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @GetMapping(
      value = "/api/criterion-types/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CriterionTypeInfo> read(@PathVariable(name = "id") String idCriterionType) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionTypeService.read(idCriterionType));
  }

  /**
   * Обрабатывает PUT запрос "/api/criterion-types/{id}" на обновление типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterionType ID типа критерия
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если тип критерия с отличным ID и данным названием уже существует
   * @throws NotFoundException если тип критерия с указанными данными не найден
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PutMapping(
      value = "/api/criterion-types/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCriterionType, @RequestBody CriterionTypeSave criterionTypeSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    criterionTypeService.update(idCriterionType, criterionTypeSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает DELETE запрос "/api/criterion-types/{id}" на удаление типа критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterionType ID типа критерия
   * @return код ответа, результат обработки запроса
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @DeleteMapping(
      value = "/api/criterion-types/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCriterionType) throws NotFoundException {

    criterionTypeService.delete(idCriterionType);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/criterion-types/partial" на получение списка типов критерия,
   * в которых нет критерий.
   * Выполнить запрос может любой клиент
   * @return список типов критерий и код ответа
   */
  @GetMapping(
      value = "/api/criterion-types/partial",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionTypeService.readAllPartial());
  }
}
