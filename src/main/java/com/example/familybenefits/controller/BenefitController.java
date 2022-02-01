package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.BenefitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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
   * Обрабатывает POST запрос "/benefit" на добавление нового пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param benefitAdd объект запроса для добавления пособия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/benefit")
  public ResponseEntity<?> addBenefit(@RequestBody BenefitAdd benefitAdd) {

    if (benefitAdd == null) {
      log.warn("POST \"/benefit\": {}", "Request body \"benefitAdd\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      benefitService.add(benefitAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены города, критерии или учреждения
      log.error("POST \"/benefit\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Пособие с указанным названием существует
      log.error("POST \"/benefit\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/benefit" на обновление пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param benefitUpdate объект запроса для обновления пособия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/benefit")
  public ResponseEntity<?> updateBenefit(@RequestBody BenefitUpdate benefitUpdate) {

    if (benefitUpdate == null) {
      log.warn("PUT \"/benefit\": {}", "Request body \"benefitUpdate\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      benefitService.update(benefitUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено пособие или не найдены города, критерии или учреждения
      log.error("PUT \"/benefit\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/benefit/{id}" на удаление пособия.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idBenefit ID пособия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/benefit/{id}")
  public ResponseEntity<?> deleteCriterion(@PathVariable(name = "id")BigInteger idBenefit) {

    try {
      benefitService.delete(idBenefit);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено пособие
      log.error("DELETE \"/benefit/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/benefit/{id}" на получение информации о пособии.
   * Выполнить запрос может любой клиент
   * @param idBenefit ID пособия
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefit/{id}")
  public ResponseEntity<BenefitInfo> getBenefit(@PathVariable(name = "id") BigInteger idBenefit) {

    try {
      BenefitInfo benefitInfo = benefitService.read(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfo);

    } catch (NotFoundException e) {
      // Не найдено пособие
      log.error("GET \"/benefit/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/benefit/all" на получение множества пособий,
   * в которых есть города, учреждения и критерии.
   * Выполнить запрос может любой клиент
   * @return множество пособий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefit/all")
  public ResponseEntity<Set<BenefitInfo>> getBenefits() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(benefitService.getAll());
  }

  /**
   * Обрабатывает GET запрос "/benefit/allpartial" на получение множества пособий,
   * в которых нет городов, учреждений или критерий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество пособий, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefit/allpartial")
  public ResponseEntity<Set<BenefitInfo>> getPartialBenefits() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(benefitService.getAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/benefit/initdata" на получение дополнительных данных для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для пособия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefit/initdata")
  public ResponseEntity<BenefitInitData> getBenefitInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(benefitService.getInitData());
  }
}
