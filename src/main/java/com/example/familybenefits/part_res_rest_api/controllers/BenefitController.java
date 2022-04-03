package com.example.familybenefits.part_res_rest_api.controllers;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.DateTimeException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInfo;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInitData;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitSave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.services.interfaces.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер запросов, связанных с пособием
 */
@RestController
public class BenefitController {

  /**
   * Интерфейс сервиса, управляющего объектом "пособие"
   */
  private final BenefitService benefitService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param benefitService интерфейс сервиса, управляющего объектом "пособие"
   */
  @Autowired
  public BenefitController(BenefitService benefitService) {
    this.benefitService = benefitService;
  }

  /**
   * Обрабатывает GET запрос "/api/benefits" на получение списки пособий,
   * в которых есть города, учреждения и критерии.
   * Фильтр по названию, ID города, учреждения или критерия.
   * Выполнить запрос может любой клиент
   * @param name название пособия
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return список пособий и код ответа
   */
  @GetMapping(
      value = "/api/benefits",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name,
                                                             @RequestParam(name = "idCity", required = false) String idCity,
                                                             @RequestParam(name = "idCriterion", required = false) String idCriterion,
                                                             @RequestParam(name = "idInstitution", required = false) String idInstitution) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(benefitService.readAllFilter(name, idCity, idCriterion, idInstitution));
  }

  /**
   * Обрабатывает POST запрос "/api/benefits" на создание пособия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param benefitSave объект запроса для сохранения пособия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдены
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PostMapping(
      value = "/api/benefits",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody BenefitSave benefitSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    benefitService.create(benefitSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/benefits/{id}" на получение информации о пособии.
   * Выполнить запрос может любой клиент
   * @param idBenefit ID пособия
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  @GetMapping(
      value = "/api/benefits/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<BenefitInfo> read(@PathVariable(name = "id") String idBenefit) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(benefitService.read(idBenefit));
  }

  /**
   * Обрабатывает PUT запрос "/api/benefits/{id}" на обновление пособия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idBenefit ID пособия
   * @param benefitSave объект запроса для сохранения пособия
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если пособие с отличным ID и данным названием уже существует
   * @throws NotFoundException если пособие с указанными данными не найдено
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PutMapping(
      value = "/api/benefits/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> update(@PathVariable(name = "id") String idBenefit, @RequestBody BenefitSave benefitSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    benefitService.update(idBenefit, benefitSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает DELETE запрос "/api/benefits/{id}" на удаление пособия.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idBenefit ID пособия
   * @return код ответа, результат обработки запроса
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  @DeleteMapping(
      value = "/api/benefits/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idBenefit) throws NotFoundException {

    benefitService.delete(idBenefit);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/benefits/partial" на получение списки пособий,
   * в которых нет городов, учреждений или критерий.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return список пособий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/benefits/partial",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(benefitService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/api/benefits/init-data" на получение дополнительных данных для пособия.
   * Данные содержат в себе списки кратких информаций о городах и полных критериях.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для пособия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/benefits/init-data",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<BenefitInitData> getInitData() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(benefitService.getInitData());
  }

  /**
   * Обрабатывает GET запрос "/api/benefits/user/{idUser}" на получение пособий пользователя.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   * @throws DateTimeException если критерии пользователя устарели
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @GetMapping(
      value = "/api/benefits/user/{idUser}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllOfUser(@PathVariable(name = "idUser") String idUser)
      throws DateTimeException, NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(benefitService.readAllOfUser(idUser));
  }
}
