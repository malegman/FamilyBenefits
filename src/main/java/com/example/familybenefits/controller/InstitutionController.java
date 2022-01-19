package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.InstitutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, звязанных с учреждением
 */
@Slf4j
@RestController
public class InstitutionController {

  /**
   * Интерфейс сервиса, управляющего объектом "учреждение"
   */
  private final InstitutionService institutionService;

  /**
   * Конструктор для инициализации интерфейса сервиса
   * @param institutionService интерфейс сервиса, управляющего объектом "учреждение"
   */
  @Autowired
  public InstitutionController(InstitutionService institutionService) {
    this.institutionService = institutionService;
  }

  /**
   * Добавляет новое учреждение
   * @param institutionAdd объект запроса для добавления учреждения
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/institution")
  public ResponseEntity<?> addInstitution(@RequestBody InstitutionAdd institutionAdd) {

    try {
      institutionService.add(institutionAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет данные учреждения
   * @param institutionUpdate объект запроса для обновления учреждения
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/institution")
  public ResponseEntity<?> updateInstitution(@RequestBody InstitutionUpdate institutionUpdate) {

    try {
      institutionService.update(institutionUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию об учреждении
   * @param idInstitution ID учреждения
   * @return информация об учреждении, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/institution/{id}")
  public ResponseEntity<InstitutionInfo> getInstitution(@PathVariable(name = "id")BigInteger idInstitution) {

    try {
      InstitutionInfo institutionInfo = institutionService.read(idInstitution);
      return ResponseEntity.status(HttpStatus.OK).body(institutionInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет учреждение
   * @param idInstitution ID учреждения
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/institution/{id}")
  public ResponseEntity<?> deleteInstitution(@PathVariable(name = "id")BigInteger idInstitution) {

    try {
      institutionService.delete(idInstitution);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает дополнительные данные для учреждения
   * @return дополнительные данные для учреждения и код ответа
   */
  @GetMapping(value = "/institution/initdata")
  public ResponseEntity<InstitutionInitData> getInstitutionInitData() {

    try {
      InstitutionInitData institutionInitData = institutionService.getInitData();
      return ResponseEntity.status(HttpStatus.OK).body(institutionInitData);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию о городе учреждения
   * @param idInstitution ID учреждения
   * @return информация о городе учреждения
   */
  @GetMapping(value = "/institution/{id}/city")
  public ResponseEntity<CityInfo> getInstitutionCity(@PathVariable(name = "id") BigInteger idInstitution) {

    try {
      CityInfo cityInfo = institutionService.readCity(idInstitution);
      return ResponseEntity.status(HttpStatus.OK).body(cityInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает множество пособий учреждения
   * @param idInstitution ID учреждения
   * @return множество пособий учреждения и код ответа
   */
  @GetMapping(value = "/institution/{id}/benefits")
  public ResponseEntity<Set<BenefitInfo>> getInstitutionBenefits(@PathVariable(name = "id") BigInteger idInstitution) {

    try {
      Set<BenefitInfo> benefitInfoSet = institutionService.readBenefits(idInstitution);
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }
}
