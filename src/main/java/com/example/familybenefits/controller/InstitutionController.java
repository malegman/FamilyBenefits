package com.example.familybenefits.controller;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionSave;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.service.s_interface.InstitutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Контроллер запросов, связанных с учреждением
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
   * Обрабатывает GET запрос "/institutions/all" на получение множества учреждений,
   * в которых есть пособия.
   * Фильтр по ID города или пособия.
   * Выполнить запрос может любой клиент
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return множество учреждений и код ответа
   */
  @GetMapping(value = "/institutions/all")
  public ResponseEntity<Set<ObjectShortInfo>> readAllFilter(@RequestParam(name = "idCity", required = false) String idCity,
                                                            @RequestParam(name = "idBenefit", required = false) String idBenefit) {

    Set<ObjectShortInfo> institutionShortInfoSet = institutionService.readAllFilter(idCity, idBenefit);
    return ResponseEntity.status(HttpStatus.OK).body(institutionShortInfoSet);
  }

  /**
   * Обрабатывает POST запрос "/institutions" на создание учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param institutionSave объект запроса для сохранения учреждения
   * @return код ответа, результат обработки запроса
   */
  @PostMapping(value = "/institutions")
  public ResponseEntity<?> create(@RequestBody InstitutionSave institutionSave) {

    if (institutionSave == null) {
      log.warn("POST \"/institutions\": {}", "Request body \"institutionSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      institutionService.create(institutionSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдены города или пособия
      log.error("POST \"/institutions\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Учреждение с указанным названием существует
      log.error("POST \"/institutions\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/institutions/{id}" на получение информации об учреждении.
   * Выполнить запрос может любой клиент
   * @param idInstitution ID учреждения
   * @return информация об учреждении, если запрос выполнен успешно, и код ответа
   */
  @GetMapping(value = "/institutions/{id}")
  public ResponseEntity<InstitutionInfo> read(@PathVariable(name = "id") String idInstitution) {

    try {
      InstitutionInfo institutionInfo = institutionService.read(idInstitution);
      return ResponseEntity.status(HttpStatus.OK).body(institutionInfo);

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("GET \"/institutions/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает PUT запрос "/institutions/{id}" на обновление учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idInstitution ID учреждения
   * @param institutionSave объект запроса для сохранения учреждения
   * @return код ответа, результат обработки запроса
   */
  @PutMapping(value = "/institutions/{id}")
  public ResponseEntity<?> update(@PathVariable(name = "id") String idInstitution,
                                  @RequestBody InstitutionSave institutionSave) {

    if (institutionSave == null) {
      log.warn("PUT \"/institutions/{id}\": {}", "Request body \"institutionSave\" is empty");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      institutionService.update(idInstitution, institutionSave);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("PUT \"/institutions/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    } catch (AlreadyExistsException e) {
      // Учреждение с отличным ID и данным названием уже существует
      log.error("PUT \"/institutions/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Обрабатывает DELETE запрос "/institutions/{id}" на удаление учреждения.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @param idInstitution ID учреждения
   * @return код ответа, результат обработки запроса
   */
  @DeleteMapping(value = "/institutions/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idInstitution) {

    try {
      institutionService.delete(idInstitution);
      return ResponseEntity.status(HttpStatus.CREATED).build();

    } catch (NotFoundException e) {
      // Не найдено учреждение
      log.error("DELETE \"/institutions/{id}\": {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Обрабатывает GET запрос "/institutions/partial" на получение множества учреждений,
   * в которых нет пособий.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return множество учреждений и код ответа
   */
  @GetMapping(value = "/institutions/partial")
  public ResponseEntity<Set<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(institutionService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/institutions/init-data" на получение дополнительных данных для учреждения.
   * Данные содержат в себе множество кратких информаций о городах и пособиях.
   * Для выполнения запроса клиент должен быть авторизован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для учреждения и код ответа
   */
  @GetMapping(value = "/institutions/init-data")
  public ResponseEntity<InstitutionInitData> getInitData() {

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(institutionService.getInitData());
  }
}
