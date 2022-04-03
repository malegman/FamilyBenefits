package com.example.familybenefits.part_res_rest_api.controllers;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInitData;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionSave;
import com.example.familybenefits.part_res_rest_api.services.interfaces.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер запросов, связанных с учреждением
 */
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
   * Обрабатывает GET запрос "/api/institutions" на получение списка учреждений,
   * в которых есть пособия.
   * Фильтр по ID города или пособия.
   * Выполнить запрос может любой клиент
   * @param name название учреждения
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return список учреждений и код ответа
   */
  @GetMapping(
      value = "/api/institutions",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllFilter(@RequestParam(name = "name", required = false) String name,
                                                             @RequestParam(name = "idCity", required = false) String idCity,
                                                             @RequestParam(name = "idBenefit", required = false) String idBenefit) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(institutionService.readAllFilter(name, idCity, idBenefit));
  }

  /**
   * Обрабатывает POST запрос "/api/institutions" на создание учреждения.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param institutionSave объект запроса для сохранения учреждения
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если не найдены город или пособия
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PostMapping(
      value = "/api/institutions",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody InstitutionSave institutionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    institutionService.create(institutionSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/institutions/{id}" на получение информации об учреждении.
   * Выполнить запрос может любой клиент
   * @param idInstitution ID учреждения
   * @return информация об учреждении, если запрос выполнен успешно, и код ответа
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @GetMapping(
      value = "/api/institutions/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<InstitutionInfo> read(@PathVariable(name = "id") String idInstitution) throws NotFoundException {

    return ResponseEntity.status(HttpStatus.OK)
        .body(institutionService.read(idInstitution));
  }

  /**
   * Обрабатывает PUT запрос "/api/institutions/{id}" на обновление учреждения.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idInstitution ID учреждения
   * @param institutionSave объект запроса для сохранения учреждения
   * @return код ответа, результат обработки запроса
   * @throws AlreadyExistsException если учреждение с отличным ID и данным названием уже существует
   * @throws NotFoundException если не найдено учреждение с указанными данными
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @PutMapping(
      value = "/api/institutions/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> update(@PathVariable(name = "id") String idInstitution, @RequestBody InstitutionSave institutionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException {

    institutionService.update(idInstitution, institutionSave);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает DELETE запрос "/api/institutions/{id}" на удаление учреждения.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @param idInstitution ID учреждения
   * @return код ответа, результат обработки запроса
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @DeleteMapping(
      value = "/api/institutions/{id}")
  public ResponseEntity<?> delete(@PathVariable(name = "id") String idInstitution) throws NotFoundException {

    institutionService.delete(idInstitution);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Обрабатывает GET запрос "/api/institutions/partial" на получение списка учреждений,
   * в которых нет пособий.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return список учреждений и код ответа
   */
  @GetMapping(
      value = "/api/institutions/partial",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<ObjectShortInfo>> readAllPartial() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(institutionService.readAllPartial());
  }

  /**
   * Обрабатывает GET запрос "/api/institutions/init-data" на получение дополнительных данных для учреждения.
   * Данные содержат в себе список кратких информаций о городах и пособиях.
   * Для выполнения запроса клиент должен быть аутентифицирован и иметь роль "ROLE_ADMIN"
   * @return дополнительные данные для учреждения и код ответа
   */
  @GetMapping(
      value = "/api/institutions/init-data",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<InstitutionInitData> getInitData() {

    return ResponseEntity.status(HttpStatus.OK)
        .body(institutionService.getInitData());
  }
}
