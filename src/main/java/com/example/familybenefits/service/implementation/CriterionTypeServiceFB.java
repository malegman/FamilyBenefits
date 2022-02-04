package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.convert.CriterionTypeDBConverter;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CriterionTypeService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "тип критерия"
 */
@Service
public class CriterionTypeServiceFB implements CriterionTypeService, EntityDBService<CriterionTypeEntity, CriterionTypeRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion_type"
   */
  private final CriterionTypeRepository criterionTypeRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозитория и сервиса
   * @param criterionTypeRepository репозиторий, работающий с моделью таблицы "criterion_type"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionTypeServiceFB(CriterionTypeRepository criterionTypeRepository, DBIntegrityService dbIntegrityService) {
    this.criterionTypeRepository = criterionTypeRepository;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет новый тип критерия
   * @param criterionTypeAdd объект запроса для добавления типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   */
  @Override
  public void add(CriterionTypeAdd criterionTypeAdd) throws AlreadyExistsException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionTypeEntity criterionTypeEntityFromAdd = CriterionTypeDBConverter
        .fromAdd(criterionTypeAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия типа критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        criterionTypeRepository::existsByName, criterionTypeEntityFromAdd.getName());

    criterionTypeRepository.saveAndFlush(criterionTypeEntityFromAdd);
  }

  /**
   * Обновляет данные типа критерия
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   */
  @Override
  public void update(CriterionTypeUpdate criterionTypeUpdate) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionTypeEntity criterionTypeEntityFromUpdate = CriterionTypeDBConverter
        .fromUpdate(criterionTypeUpdate, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeRepository::existsById, criterionTypeEntityFromUpdate);

    criterionTypeRepository.saveAndFlush(criterionTypeEntityFromUpdate);
  }

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @Override
  public void delete(String idCriterionType) throws NotFoundException {

    String prepareIdCriterionType = dbIntegrityService.preparePostgreSQLString(idCriterionType);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeRepository::existsById, prepareIdCriterionType);

    criterionTypeRepository.deleteById(prepareIdCriterionType);
  }

  /**
   * Возвращает информацию о типе критерия по его ID
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @Override
  public CriterionTypeInfo read(String idCriterionType) throws NotFoundException {

    String prepareIdCriterionType = dbIntegrityService.preparePostgreSQLString(idCriterionType);

    // Получение типа критерия по его ID, если тип критерия существует
    CriterionTypeEntity criterionTypeEntityFromRequest = criterionTypeRepository.findById(prepareIdCriterionType)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Criterion type with ID \"%s\" not found", idCriterionType)));

    return CriterionTypeDBConverter.toInfo(criterionTypeEntityFromRequest);
  }

  /**
   * Возвращает множество типов критерия, в которых есть критерии
   * @return множество информаций о типах критерий
   */
  @Override
  public Set<CriterionTypeInfo> getAll() {

    return findAllFull()
        .stream()
        .map(CriterionTypeDBConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество типов критерия, в которых нет критерий
   * @return множество информаций о типах критерий
   */
  @Override
  public Set<CriterionTypeInfo> getAllPartial() {

    return findAllPartial()
        .stream()
        .map(CriterionTypeDBConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  @Override
  public CriterionTypeRepository getRepository() {
    return criterionTypeRepository;
  }

  /**
   * Возвращает множество моделей таблицы "criterion_type", в которых есть модели критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<CriterionTypeEntity> findAllFull() {

    return criterionTypeRepository
        .findAll()
        .stream()
        .filter(criterionTypeEntity -> !criterionTypeEntity.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "criterion_type", в которых нет моделей критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<CriterionTypeEntity> findAllPartial() {

    return criterionTypeRepository
        .findAll()
        .stream()
        .filter(criterionTypeEntity -> criterionTypeEntity.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
