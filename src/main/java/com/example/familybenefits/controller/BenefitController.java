package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitSave;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.DateTimeException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.BenefitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, связанных с пособием
 */
@Slf4j
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
   * Обрабатывает GET запрос "/benefits/all" на получение множества пособий,
   * в которых есть города, учреждения и критерии.
   * Фильтр по ID города, учреждения или критерия.
   * Выполнить запрос может любой клиент
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return множество пособий и код ответа
   */
  @GetMapping(value = "/benefits")
  public ResponseEntity<Set<ObjectShortInfo>> readAllFilter(@RequestParam(name = "idCity", required = false) String idCity,
                                                            @RequestParam(name = "idCriterion", required = false) String idCriterion,
                                                            @RequestParam(name = "idInstitution", required = false) String idInstitution) {

    Set<ObjectShortInfo> benefitShortInfoSet = benefitService.readAllFilter(idCity, idCriterion, idInstitution);
    return ResponseEntity.status(HttpStatus.CREATED).body(benefitShortInfoSet);
  }

  /**
   * Обрабатывает POST запрос "/benefits" на создание пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param benefitSave объект запроса для сохранения пособия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/benefits")
  public ResponseEntity<?> create(@RequestBody BenefitSave benefitSave) {

    if (benefitSave == null) {
      log.warn("POST \"/benefits\": {}", "Request body \"benefitSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      benefitService.create(benefitSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены города или критерии
      log.error("POST \"/benefits\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Пособие с указанным названием существует
      log.error("POST \"/benefits\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/benefits/{id}" на получение информации о пособии.
   * Выполнить запрос может любой клиент
   * @param idBenefit ID пособия
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefits/{id}")
  public ResponseEntity<BenefitInfo> read(@PathVariable(name = "id") String idBenefit) {

    try {
      BenefitInfo benefitInfo = benefitService.read(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfo);

    } catch (NotFoundException e) {
      // Не найдено пособие
      log.error("GET \"/benefits/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/benefits/{id}" на обновление пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idBenefit ID пособия
   * @param benefitSave объект запроса для сохранения пособия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/benefits/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idBenefit,
                                  @RequestBody BenefitSave benefitSave) {

    if (benefitSave == null) {
      log.warn("PUT \"/benefits/{id}\": {}", "Request body \"benefitSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      benefitService.update(idBenefit, benefitSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено пособие или не найдены города или критерии
      log.error("PUT \"/benefits/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/benefits/{id}" на удаление пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idBenefit ID пособия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/benefits/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idBenefit) {

    try {
      benefitService.delete(idBenefit);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено пособие
      log.error("DELETE \"/benefits/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/benefits/partial" на получение множества пособий,
   * в которых нет городов, учреждений или критерий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество пособий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefits/partial")
  public ResponseEntity<Set<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(benefitService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/benefits/init-data" на получение дополнительных данных для пособия.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для пособия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefits/init-data")
  public ResponseEntity<BenefitInitData> getInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(benefitService.getInitData());
  }

  /**
   * Обрабатывает GET запрос "/benefits/user/{idUser}" на получение пособий пользователя.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_USER"
   * @param idUser ID пользователя
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefits/user/{idUser}")
  public ResponseEntity<Set<ObjectShortInfo>> readAllOfUser(@PathVariable(name = "idUser") String idUser) {

    try {
      Set<ObjectShortInfo> benefitShortInfoSet = benefitService.readAllOfUser(idUser);
      return ResponseEntity.status(HttpStatus.OK).body(benefitShortInfoSet);

    } catch (NotFoundException e) {
      // Не найден пользователь
      log.error("GET \"/benefits/user/{idUser}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());

    } catch (DateTimeException e) {
      // Устарели критерии
      log.error("GET \"/benefits/user/{idUser}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptySet());
    }
  }
}
