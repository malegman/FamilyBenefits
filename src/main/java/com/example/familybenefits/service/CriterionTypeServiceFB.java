package com.example.familybenefits.service;

import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.CriterionTypeConverter;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "тип критерия"
 */
@Service
public class CriterionTypeServiceFB implements CriterionTypeService {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion_type"
   */
  private final CriterionTypeRepository criterionTypeRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param criterionTypeRepository репозиторий, работающий с моделью таблицы "criterion_type"
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionTypeServiceFB(CriterionTypeRepository criterionTypeRepository, CriterionRepository criterionRepository,
                                DBIntegrityService dbIntegrityService) {
    this.criterionTypeRepository = criterionTypeRepository;
    this.criterionRepository = criterionRepository;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет новый тип критерия
   * @param criterionTypeAdd объект запроса для добавления типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   */
  @Override
  public void add(CriterionTypeAdd criterionTypeAdd) throws AlreadyExistsException {

    dbIntegrityService.checkAbsenceByUniqStrElseThrow(
        criterionTypeRepository::existsByName, criterionTypeAdd.getName(),
        "The criterion type with name %s already exists");

    criterionTypeRepository.saveAndFlush((CriterionTypeEntity) CriterionTypeConverter
        .fromAdd(criterionTypeAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Обновляет данные типа критерия
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   */
  @Override
  public void update(CriterionTypeUpdate criterionTypeUpdate) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        criterionTypeRepository::existsById, criterionTypeUpdate.getId(),
        "Criterion type with ID %s not found");

    criterionTypeRepository.saveAndFlush((CriterionTypeEntity) CriterionTypeConverter
        .fromUpdate(criterionTypeUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  public void delete(BigInteger idCriterionType) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        criterionTypeRepository::existsById, idCriterionType,
        "Criterion type with ID %s not found");

    criterionTypeRepository.deleteById(idCriterionType);
  }

  /**
   * Возвращает информацию о типе критерия по его ID
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  public CriterionTypeInfo read(BigInteger idCriterionType) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        criterionTypeRepository::existsById, idCriterionType,
        "Criterion type with ID %s not found");

    return CriterionTypeConverter.toInfo(criterionTypeRepository.getById(idCriterionType));
  }

  /**
   * Возвращает множество всех типов критерий
   * @return множество информаций о типах критерий
   * @throws NotFoundException если типы критерий не найдены
   */
  public Set<CriterionTypeInfo> readAll() throws NotFoundException {

    Set<CriterionTypeInfo> criterionTypeInfoSet = criterionTypeRepository
        .findAll()
        .stream()
        .map(CriterionTypeConverter::toInfo)
        .collect(Collectors.toSet());
    if (criterionTypeInfoSet.isEmpty()) {
      throw new NotFoundException("Criterion types not found");
    }

    return criterionTypeInfoSet;
  }

  /**
   * Возвращает множество всех критерий типа критерия
   * @param idCriterionType ID типа критерия
   * @return множество информаций о критериях типа критерия
   * @throws NotFoundException если критерии не найдены или тип критерия с указынным ID не найден
   */
  public Set<CriterionInfo> readCriteria(BigInteger idCriterionType) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        criterionTypeRepository::existsById, idCriterionType,
        "Criterion type with ID %s not found");

    Set<CriterionInfo> criterionInfoSet = criterionRepository
        .findAllByCriterionType(new CriterionTypeEntity(idCriterionType))
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Criteria of criterion type with id %s not found", idCriterionType));
    }

    return criterionInfoSet;
  }
}
