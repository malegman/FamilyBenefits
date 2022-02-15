package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CitySave;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import com.example.familybenefits.service.s_interface.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Контроллер запросов, связанных с городом
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
   * Обрабатывает GET запрос "/cities/all" на получение множества городов,
   * в которых есть учреждения и пособия.
   * Фильтр по названию или ID пособия.
   * Выполнить запрос может любой клиент
   * @param name Название города
   * @param idBenefit ID пособия
   * @return множество городов, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/cities")
  public ResponseEntity<Set<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name,
                                                            @RequestParam(name = "idBenefit", required = false) String idBenefit) {

    Set<ObjectShortInfo> cityShortInfoSet = cityService.readAllFilter(name, idBenefit);
    return ResponseEntity.status(HttpStatus.OK).body(cityShortInfoSet);
  }

  /**
   * Обрабатывает POST запрос "/cities" на создание города.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param citySave объект запроса для сохранения города
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/cities")
  public ResponseEntity<?> create(@RequestBody CitySave citySave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если тело запроса пустое
    if (citySave == null) {
      log.warn("{} POST \"/cities\": Request body \"citySave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      cityService.create(citySave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      // Город с указанным названием существует
      log.error("{} POST \"/cities\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (NotFoundException e) {
      // Не найдены пособия
      log.error("{} POST \"/cities\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/cities/{id}" на получение информации о городе.
   * Выполнить запрос может любой клиент
   * @param idCity ID города
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return информация о городе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/cities/{id}")
  public ResponseEntity<CityInfo> read(@PathVariable(name = "id") String idCity,
                                       @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    try {
      CityInfo cityInfo = cityService.read(idCity);
      return ResponseEntity.status(HttpStatus.OK).body(cityInfo);

    } catch (NotFoundException e) {
      // Не найден город
      log.error("{} GET \"/cities/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/cities/{id}" на обновление города.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCity ID города
   * @param citySave объект запроса для сохранения города
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/cities/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCity,
                                  @RequestBody CitySave citySave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если тело запроса пустое
    if (citySave == null) {
      log.warn("{} PUT \"/cities/{id}\": Request body \"citySave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      cityService.update(idCity, citySave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден город или не найдены пособия
      log.error("{} PUT \"/cities/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Город с отличным ID и данным названием уже существует
      log.error("{} PUT \"/cities/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/cities/{id}" на удаление городе.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCity ID города
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/cities/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCity,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    try {
      cityService.delete(idCity);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден город
      log.error("{} DELETE \"/cities/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/cities/partial" на получение множества городов,
   * в которых нет учреждений или пособий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество городов, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/cities/partial")
  public ResponseEntity<Set<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(cityService.readAllPartial());
  }
}
