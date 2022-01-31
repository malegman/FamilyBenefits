package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.convert.CriterionTypeConverter;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CriterionTypeService;
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
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
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
    CriterionTypeEntity criterionTypeEntity = (CriterionTypeEntity) CriterionTypeConverter
        .fromAdd(criterionTypeAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия типа критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        criterionTypeRepository::existsByName, criterionTypeEntity.getName(),
        "The criterion type with name %s already exists");

    criterionTypeRepository.saveAndFlush(criterionTypeEntity);
  }

  /**
   * Обновляет данные типа критерия
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   */
  @Override
  public void update(CriterionTypeUpdate criterionTypeUpdate) throws NotFoundException {

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionTypeRepository::existsById, criterionTypeUpdate.getId(),
        "Criterion type with ID %s not found");

    // Сохранение полученной модели таблицы из запроса с подготовленными строковыми значениями для БД
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

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
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

    // Получение типа критерия по его ID, если тип критерия существует
    CriterionTypeEntity criterionTypeEntityFromRequest = criterionTypeRepository.findById(idCriterionType)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Criterion type with ID %s not found", idCriterionType)));

    return CriterionTypeConverter.toInfo(criterionTypeEntityFromRequest);
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
}
