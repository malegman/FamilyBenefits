package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionSave;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.web.authentication.JwtAuthenticationUserData;
import com.example.familybenefits.service.s_interface.CriterionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
   * Обрабатывает GET запрос "/criteria" на получение множества критерий,
   * в которых есть пособия.
   * Фильтр по ID пособия или типа критерия.
   * Выполнить запрос может любой клиент
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return множество критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteria")
  public ResponseEntity<Set<ObjectShortInfo>> readAllFilter(@RequestParam(name = "idBenefit", required = false) String idBenefit,
                                                          @RequestParam(name = "idCriterionType", required = false) String idCriterionType) {

    Set<ObjectShortInfo> criterionShortInfoSet = criterionService.readAllFilter(idBenefit, idCriterionType);
    return ResponseEntity.status(HttpStatus.OK).body(criterionShortInfoSet);
  }

  /**
   * Обрабатывает POST запрос "/criteria" на создание критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param criterionSave объект запроса для сохранения критерия
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/criteria")
  public ResponseEntity<?> create(@RequestBody CriterionSave criterionSave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если тело запроса пустое
    if (criterionSave == null) {
      log.warn("{} POST \"/criteria\": Request body \"criterionSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionService.create(criterionSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден тип критерия
      log.error("{} POST \"/criteria\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Критерий с указанным названием существует
      log.error("{} POST \"/criteria\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criteria/{id}" на получение информации о критерии.
   * Выполнить запрос может любой клиент
   * @param idCriterion ID критерия
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return информация о критерии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteria/{id}")
  public ResponseEntity<CriterionInfo> read(@PathVariable(name = "id") String idCriterion,
                                            @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    try {
      CriterionInfo criterionInfo = criterionService.read(idCriterion);
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfo);

    } catch (NotFoundException e) {
      // Не найден критерий
      log.error("{} GET \"/criteria/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/criteria/{id}" на обновление критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCriterion ID критерия
   * @param criterionSave объект запроса для сохранения критерия
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/criteria/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idCriterion,
                                  @RequestBody CriterionSave criterionSave,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если тело запроса пустое
    if (criterionSave == null) {
      log.warn("{} PUT \"/criteria/{id}\": Request body \"criterionSave\" is empty", userIp);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      criterionService.update(idCriterion, criterionSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден критерий или тип критерия
      log.error("{} PUT \"/criteria/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Критерий с отличным ID и данным названием уже существует
      log.error("{} PUT \"/criteria/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/criteria/{id}" на удаление критерия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idCriterion ID критерия
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/criteria/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idCriterion,
                                  @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    try {
      criterionService.delete(idCriterion);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найден критерий
      log.error("{} DELETE \"/criteria/{id}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/criteria/partial" на получение множества критерий,
   * в которых нет пособий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество критерий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteria/partial")
  public ResponseEntity<Set<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/criteria/init-data" на получение дополнительных данных для критерия.
   * Данные содержат в себе множество кратких информаций о типах критерий
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteria/init-data")
  public ResponseEntity<CriterionInitData> getInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(criterionService.getInitData());
  }

  /**
   * Обрабатывает GET запрос "/criteria/user/{idUser}" на получение критерий пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @param userAuth данные пользователя из jwt, отправившего запрос
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteria/user/{idUser}")
  public ResponseEntity<Set<ObjectShortInfo>> readAllOfUser(@PathVariable(name = "idUser") String idUser,
                                                            @AuthenticationPrincipal JwtAuthenticationUserData userAuth) {

    String userIp = userAuth.getIpAddress();

    // Если пользователь пытается получить не свои критерии
    if (!userAuth.getIdUser().equals(idUser)) {
      log.warn("{} GET \"/benefits/user/{idUser}\": User with id {} tried to read criteria of user with id {}", userIp, userAuth.getIdUser(), idUser);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    try {
      Set<ObjectShortInfo> criterionShortInfoSet = criterionService.readAllOfUser(idUser);
      return ResponseEntity.status(HttpStatus.OK).body(criterionShortInfoSet);

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("{} GET \"/benefits/user/{idUser}\": {}", userIp, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }
}
