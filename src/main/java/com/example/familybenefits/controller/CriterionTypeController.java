package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.CriterionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, звязанных с типом критерия
 */
@Slf4j
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
   * Добавляет новый тип критерия
   * @param criterionTypeAdd объект запроса для добавления типа критерия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/criteriontype")
  public ResponseEntity<?> addCriterionType(@RequestBody CriterionTypeAdd criterionTypeAdd) {

    try {
      criterionTypeService.add(criterionTypeAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет данные типа критерия
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/criteriontype")
  public ResponseEntity<?> updateCriterionType(@RequestBody CriterionTypeUpdate criterionTypeUpdate) {

    try {
      criterionTypeService.update(criterionTypeUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию о типе критерия
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/criteriontype/{id}")
  public ResponseEntity<CriterionTypeInfo> getCriterionType(@PathVariable(name = "id")BigInteger idCriterionType) {

    try {
      CriterionTypeInfo criterionTypeInfo = criterionTypeService.read(idCriterionType);
      return ResponseEntity.status(HttpStatus.OK).body(criterionTypeInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет тип критерия
   * @param idCriterionType ID типа критерия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/criteriontype/{id}")
  public ResponseEntity<?> deleteCriterionType(@PathVariable(name = "id")BigInteger idCriterionType) {

    try {
      criterionTypeService.delete(idCriterionType);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает множество типов критериев
   * @return множество типов критериев и код ответа
   */
  @GetMapping(value = "/criteriontype/all")
  public ResponseEntity<Set<CriterionTypeInfo>> getCriterionTypes() {

    try {
      Set<CriterionTypeInfo> criterionTypeInfoSet = criterionTypeService.readAll();
      return ResponseEntity.status(HttpStatus.OK).body(criterionTypeInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество критерий типа критерия
   * @param idCriterionType ID типа критерия
   * @return множество критерий типа критерия и код ответа
   */
  @GetMapping(value = "/criteriontype/{id}/criteria")
  public ResponseEntity<Set<CriterionInfo>> getCityBenefits(@PathVariable(name = "id") BigInteger idCriterionType) {

    try {
      Set<CriterionInfo> criterionInfoSet = criterionTypeService.readCriteria(idCriterionType);
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }
}
