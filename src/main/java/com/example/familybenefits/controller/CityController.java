package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
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
   * Добавляет новый город
   * @param cityAdd объект запроса для добавления города
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/city")
  public ResponseEntity<?> addCity(@RequestBody CityAdd cityAdd) {

    try {
      cityService.add(cityAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (AlreadyExistsException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет город
   * @param cityUpdate объект запроса для обновления города
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/city")
  public ResponseEntity<?> updateCity(@RequestBody CityUpdate cityUpdate) {

    try {
      cityService.update(cityUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию о городе
   * @param idCity ID города
   * @return информация о городе, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/city/{id}")
  public ResponseEntity<CityInfo> getCity(@PathVariable(name = "id") BigInteger idCity) {

    try {
      CityInfo cityInfo = cityService.read(idCity);
      return ResponseEntity.status(HttpStatus.OK).body(cityInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет город
   * @param idCity ID города
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/city/{id}")
  public ResponseEntity<?> deleteCity(@PathVariable(name = "id") BigInteger idCity) {

    try {
      cityService.delete(idCity);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает множество всех городов
   * @return множество городов и код ответа
   */
  @GetMapping(value = "/city/all")
  public ResponseEntity<Set<CityInfo>> getCities() {

    try {
      Set<CityInfo> cities = cityService.readAll();
      return ResponseEntity.status(HttpStatus.OK).body(cities);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество учреждений города
   * @param idCity ID города
   * @return множество учреждений города и код ответа
   */
  @GetMapping(value = "/city/{id}/institutions")
  public ResponseEntity<Set<InstitutionInfo>> getCityInstitutions(@PathVariable(name = "id") BigInteger idCity) {

    try {
      Set<InstitutionInfo> institutions = cityService.readInstitutions(idCity);
      return ResponseEntity.status(HttpStatus.OK).body(institutions);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество пособий города
   * @param idCity ID города
   * @return множество пособий города и код ответа
   */
  @GetMapping(value = "/city/{id}/benefits")
  public ResponseEntity<Set<BenefitInfo>> getCityBenefits(@PathVariable(name = "id") BigInteger idCity) {

    try {
      Set<BenefitInfo> benefits = cityService.readBenefits(idCity);
      return ResponseEntity.status(HttpStatus.OK).body(benefits);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }
}
