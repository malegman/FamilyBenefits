package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityInitData;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, звязанных с городом
 */
@Slf4j
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
   * Обрабатывает POST запрос "/city" на добавление нового города.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param cityAdd объект запроса для добавления города
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/city")
  public ResponseEntity<?> addCity(@RequestBody CityAdd cityAdd) {

    if (cityAdd == null) {
      log.warn("POST \"/city/\": " + "Request body \"cityAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      cityService.add(cityAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      // Город с указанным названием существует
      log.error("POST \"/city/\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (NotFoundException e) {
      // Не найдены пособия
      log.error("POST \"/city/\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/city" на обновление города.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param cityUpdate объект запроса для обновления города
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/city")
  public ResponseEntity<?> updateCity(@RequestBody CityUpdate cityUpdate) {

    if (cityUpdate == null) {
      log.warn("PUT \"/city/\": " + "Request body \"cityUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      cityService.update(cityUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден город или не найдены пособия
      log.error("PUT \"/city/\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/city/{id}" на удаление городе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCity ID города
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/city/{id}")
  public ResponseEntity<?> deleteCity(@PathVariable(name = "id") BigInteger idCity) {

    try {
      cityService.delete(idCity);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден город
      log.error("DELETE \"/city/{id}\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/city/{id}" на получение информации о городе.
   * Выполнить запрос может любой клиент
   * @param idCity ID города
   * @return информация о городе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/city/{id}")
  public ResponseEntity<CityInfo> getCity(@PathVariable(name = "id") BigInteger idCity) {

    try {
      CityInfo cityInfo = cityService.read(idCity);
      return ResponseEntity.status(HttpStatus.OK).body(cityInfo);

    } catch (NotFoundException e) {
      // Не найден город
      log.error("GET \"/city/{id}\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/city/all" на получение множества всех городов
   * Выполнить запрос может любой клиент
   * @return множество городов, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/city/all")
  public ResponseEntity<Set<CityInfo>> getCities() {

    try {
      Set<CityInfo> cityInfoSet = cityService.readAll();
      return ResponseEntity.status(HttpStatus.OK).body(cityInfoSet);

    } catch (NotFoundException e) {
      // Не найдены города
      log.error("GET \"/city/all\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Обрабатывает GET запрос "/city/initdata" на получение дополнительных данных для города.
   * Данные содержат в себе множества кратких информаций о пособиях.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для города, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/city/initdata")
  public ResponseEntity<CityInitData> getCityInitData() {

    try {
      CityInitData cityInitData = cityService.getInitData();
      return ResponseEntity.status(HttpStatus.OK).body(cityInitData);

    } catch (NotFoundException e) {
      // Не найдены пособия
      log.error("GET \"/city/initdata\": " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
