package com.example.familybenefits.part_res_rest_api.controllers;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInitData;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionSave;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер запросов, связанных с критерием
 */
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
   * Обрабатывает GET запрос "/api/criteria" на получение списка критерий,
   * в которых есть пособия.
   * Фильтр по ID пособия или типа критерия.
   * Выполнить запрос может любой клиент
   * @param name название критерия
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return список критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/criteria",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name,
                                                             @RequestParam(name = "idBenefit", required = false) String idBenefit, 
                                                             @RequestParam(name = "idCriterionType", required = false) String idCriterionType) {
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionService.readAllFilter(name, idBenefit, idCriterionType));
  }

  /**
   * Обрабатывает POST запрос "/api/criteria" на создание критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param criterionSave объект запроса для сохранения критерия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия данного критерия с указанным ID не найдены
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PostMapping(
      value = "/api/criteria",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody CriterionSave criterionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    criterionService.create(criterionSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/criteria/{id}" на получение информации о критерии.
   * Выполнить запрос может любой клиент
   * @param idCriterion ID критерия
   * @return информация о критерии, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @GetMapping(
      value = "/api/criteria/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CriterionInfo> read(@PathVariable(name = "id") String idCriterion) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionService.read(idCriterion));
  }

  /**
   * Обрабатывает PUT запрос "/api/criteria/{id}" на обновление критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterion ID критерия
   * @param criterionSave объект запроса для сохранения критерия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если критерий с отличным ID и данным названием уже существует
   * @throws NotFoundException если критерий с указанными данными не найден
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PutMapping(
      value = "/api/criteria/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCriterion, @RequestBody CriterionSave criterionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    criterionService.update(idCriterion, criterionSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает DELETE запрос "/api/criteria/{id}" на удаление критерия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCriterion ID критерия
   * @return код ответа, результат обработки запроса
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @DeleteMapping(
      value = "/api/criteria/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCriterion) throws NotFoundException {

    criterionService.delete(idCriterion);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/criteria/partial" на получение списка критерий,
   * в которых нет пособий.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return список критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/criteria/partial",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/api/criteria/init-data" на получение дополнительных данных для критерия.
   * Данные содержат в себе список кратких информаций о типах критерий
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/api/criteria/init-data")
  public ResponseEntity<CriterionInitData> getInitData() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionService.getInitData());
  }

  /**
   * Обрабатывает GET запрос "/api/criteria/user/{idUser}" на получение критерий пользователя.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @GetMapping(
      value = "/api/criteria/user/{idUser}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllOfUser(@PathVariable(name = "idUser") String idUser) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(criterionService.readAllOfUser(idUser));
  }
}
