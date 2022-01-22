package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.BenefitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;

/**
 * Контроллер запросов, звязанных с пособием
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
   * Добавляет новое пособие
   * @param benefitAdd объект запроса для добавления пособия
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/benefit")
  public ResponseEntity<?> addBenefit(@RequestBody BenefitAdd benefitAdd) {

    try {
      benefitService.add(benefitAdd);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException nfe) {
      log.error(nfe.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    } catch (AlreadyExistsException aee) {
      log.error(aee.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обновляет пособие
   * @param benefitUpdate объект запроса для обновления пособия
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/benefit")
  public ResponseEntity<?> updateBenefit(@RequestBody BenefitUpdate benefitUpdate) {

    try {
      benefitService.update(benefitUpdate);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает информацию о пособии
   * @param idBenefit ID пособия
   * @return информация о пособии, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/benefit/{id}")
  public ResponseEntity<BenefitInfo> getBenefit(@PathVariable(name = "id") BigInteger idBenefit) {

    try {
      BenefitInfo benefitInfo = benefitService.read(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfo);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Удаляет пособие
   * @param idBenefit ID пособия
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/benefit/{id}")
  public ResponseEntity<?> deleteCriterion(@PathVariable(name = "id")BigInteger idBenefit) {

    try {
      benefitService.delete(idBenefit);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для критерия и код ответа
   */
  @GetMapping(value = "/benefit/initdata")
  public ResponseEntity<BenefitInitData> getBenefitInitData() {

    try {
      BenefitInitData benefitInitData = benefitService.getInitData();
      return ResponseEntity.status(HttpStatus.OK).body(benefitInitData);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Возвращает множество всех полных пособий - с городом, учреждением и критерием
   * @return множество критерий и код ответа
   */
  @GetMapping(value = "/benefit/all")
  public ResponseEntity<Set<BenefitInfo>> getBenefits() {

    try {
      Set<BenefitInfo> benefitInfoSet = benefitService.readAllFull();
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество всех неполных пособий - без города, учреждения или критерия
   * @return множество критерий и код ответа
   */
  @GetMapping(value = "/benefit/allpartial")
  public ResponseEntity<Set<BenefitInfo>> getPartialBenefits() {

    try {
      Set<BenefitInfo> benefitInfoSet = benefitService.readAllPartial();
      return ResponseEntity.status(HttpStatus.OK).body(benefitInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество городов пособия
   * @param idBenefit ID пособия
   * @return множество городов пособия и код ответа
   */
  @GetMapping(value = "/benefit/{id}/cities")
  public ResponseEntity<Set<CityInfo>> getBenefitCities(@PathVariable(name = "id") BigInteger idBenefit) {

    try {
      Set<CityInfo> cityInfoSet = benefitService.readCities(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(cityInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество учреждений пособия
   * @param idBenefit ID пособия
   * @return множество учреждений пособия и код ответа
   */
  @GetMapping(value = "/benefit/{id}/institutions")
  public ResponseEntity<Set<InstitutionInfo>> getBenefitInstitutions(@PathVariable(name = "id") BigInteger idBenefit) {

    try {
      Set<InstitutionInfo> institutionInfoSet = benefitService.readInstitutions(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(institutionInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }

  /**
   * Возвращает множество полных критерий пособия
   * @param idBenefit ID пособия
   * @return множество критерий пособия и код ответа
   */
  @GetMapping(value = "/benefit/{id}/criteria")
  public ResponseEntity<Set<CriterionInfo>> getBenefitCriteria(@PathVariable(name = "id") BigInteger idBenefit) {

    try {
      Set<CriterionInfo> criterionInfoSet = benefitService.readCriteria(idBenefit);
      return ResponseEntity.status(HttpStatus.OK).body(criterionInfoSet);

    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
    }
  }
}
