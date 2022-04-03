package com.example.familybenefits.part_res_rest_api.controllers;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.city.CityInfo;
import com.example.familybenefits.part_res_rest_api.api_model.city.CitySave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер запросов, связанных с городом
 */
@RestController
public class CityController {

  /**
   * Интерфейс сервиса, управляющего объектом "город"
   */
  private final CityService cityService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param cityService интерфейс сервиса, управляющего объектом "город"
   */
  @Autowired
  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  /**
   * Обрабатывает GET запрос "/api/cities" на получение списка городов,
   * в которых есть учреждения и пособия.
   * Фильтр по названию или ID пособия или учреждения.
   * Выполнить запрос может любой клиент
   * @param name название города
   * @param idBenefit ID пособия
   * @param idInstitution ID учреждения
   * @return список городов, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/cities",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name, 
                                                             @RequestParam(name = "idBenefit", required = false) String idBenefit,
                                                             @RequestParam(name = "idInst", required = false) String idInstitution) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(cityService.readAllFilter(name, idBenefit, idInstitution));
  }

  /**
   * Обрабатывает POST запрос "/api/cities" на создание города.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param citySave объект запроса для сохранения города
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PostMapping(
      value = "/api/cities",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody CitySave citySave)
      throws AlreadyExistsException, InvalidStringException {

    cityService.create(citySave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/cities/{id}" на получение информации о городе.
   * Выполнить запрос может любой клиент
   * @param idCity ID города
   * @return информация о городе, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если город с указанным ID не найден
   */
  @GetMapping(
      value = "/api/cities/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CityInfo> read(@PathVariable(name = "id") String idCity) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(cityService.read(idCity));
  }

  /**
   * Обрабатывает PUT запрос "/api/cities/{id}" на обновление города.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCity ID города
   * @param citySave объект запроса для сохранения города
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если город с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   * @throws NotFoundException если город с указанными данными не найден
   */
  @PutMapping(
      value = "/api/cities/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCity, @RequestBody CitySave citySave)
      throws AlreadyExistsException, InvalidStringException, NotFoundException {

    cityService.update(idCity, citySave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает DELETE запрос "/api/cities/{id}" на удаление городе.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idCity ID города
   * @return код ответа, результат обработки запроса
   * @throws NotFoundException если город с указанным ID не найден
   */
  @DeleteMapping(
      value = "/api/cities/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCity) throws NotFoundException {

    cityService.delete(idCity);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/cities/partial" на получение списка городов,
   * в которых нет учреждений или пособий.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return список городов, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(
      value = "/api/cities/partial",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(cityService.readAllPartial());
  }
}
