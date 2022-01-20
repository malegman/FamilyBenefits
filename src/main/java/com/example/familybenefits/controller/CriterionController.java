package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.CriterionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, звязанных с критерием
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
   * Добавляет новый критерий
   * @param criterionAdd объект запроса для добавления критерия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/criterion")
  public ResponseEntity<?> addCriterion(@RequestBody CriterionAdd criterionAdd) {

    try {
      criterionService.add(criterionAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/criterion")
  public ResponseEntity<?> updateCriterion(@RequestBody CriterionUpdate criterionUpdate) {

    try {
      criterionService.update(criterionUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию о критерии
   * @param idCriterion ID критерия
   * @return информация о критерии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criterion/{id}")
  public ResponseEntity<CriterionInfo> getCriterion(@PathVariable(name = "id") BigInteger idCriterion) {

    try {
      CriterionInfo criterionInfo = criterionService.read(idCriterion);
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет критерий
   * @param idCriterion ID критерия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/criterion/{id}")
  public ResponseEntity<?> deleteCriterion(@PathVariable(name = "id")BigInteger idCriterion) {

    try {
      criterionService.delete(idCriterion);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе множетсво кратких информаций о типах критерий
   * @return дополнительные данные для критерия и код ответа
   */
  @GetMapping(value = "/criterion/initdata")
  public ResponseEntity<CriterionInitData> getCriterionInitData() {

    try {
      CriterionInitData criterionInitData = criterionService.getInitData();
      return ResponseEntity.status(HttpStatus.OK).body(criterionInitData);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает множество всех полных критерий - с типом критерия
   * @return множество критерий и код ответа
   */
  @GetMapping(value = "/criterion/all")
  public ResponseEntity<Set<CriterionInfo>> getCriteria() {

    try {
      Set<CriterionInfo> criterionInfoSet = criterionService.readAllFull();
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество всех неполных критерий - без типа критерия
   * @return множество критерий и код ответа
   */
  @GetMapping(value = "/criterion/allpartial")
  public ResponseEntity<Set<CriterionInfo>> getPartialCriteria() {

    try {
      Set<CriterionInfo> criterionInfoSet = criterionService.readAllPartial();
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает информацию о типе критерия критерия
   * @param idCriterion ID критерия
   * @return информация о типе критерия критерия
   */
  @GetMapping(value = "/criterion/{id}/criteriontype")
  public ResponseEntity<CriterionTypeInfo> getCriterionCriterionType(@PathVariable(name = "id") BigInteger idCriterion) {

    try {
      CriterionTypeInfo criterionTypeInfo = criterionService.readCriterionType(idCriterion);
      return ResponseEntity.status(HttpStatus.OK).body(criterionTypeInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
