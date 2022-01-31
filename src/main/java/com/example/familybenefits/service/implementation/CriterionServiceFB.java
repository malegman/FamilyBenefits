package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.CriterionTypeConverter;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "критерий"
 */
@Service
public class CriterionServiceFB implements CriterionService {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "criterion_type"
   */
  private final CriterionTypeRepository criterionTypeRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param criterionTypeRepository репозиторий, работающий с моделью таблицы "criterion_type"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository, CriterionTypeRepository criterionTypeRepository,
                            DBIntegrityService dbIntegrityService) {
    this.criterionRepository = criterionRepository;
    this.criterionTypeRepository = criterionTypeRepository;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет новый критерий
   * @param criterionAdd объект запроса для добавления критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия критерия с указанным ID не найден
   */
  @Override
  public void add(CriterionAdd criterionAdd) throws AlreadyExistsException, NotFoundException {

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionTypeRepository::existsById, criterionAdd.getIdCriterionType(),
        "Criterion type with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromAdd = (CriterionEntity) CriterionConverter
        .fromAdd(criterionAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        criterionTypeRepository::existsByName, criterionEntityFromAdd.getName(),
        "The criterion with name %s already exists");

    criterionRepository.saveAndFlush(criterionEntityFromAdd);
  }

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   */
  @Override
  public void update(CriterionUpdate criterionUpdate) throws NotFoundException {

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionTypeRepository::existsById, criterionUpdate.getIdCriterionType(),
        "Criterion type with ID %s not found");

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionTypeRepository::existsById, criterionUpdate.getId(),
        "Criterion with ID %s not found");

    // Сохранение полученной модели таблицы из запроса с подготовленными строковыми значениями для БД
    criterionRepository.saveAndFlush((CriterionEntity) CriterionConverter
        .fromUpdate(criterionUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Удаляет критерий по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idCriterion) throws NotFoundException {

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionTypeRepository::existsById, idCriterion,
        "Criterion with ID %s not found");

    criterionRepository.deleteById(idCriterion);
  }

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public CriterionInfo read(BigInteger idCriterion) throws NotFoundException {

    // Получение критерия по его ID, если критерий существует
    CriterionEntity criterionEntityFromRequest = criterionRepository.findById(idCriterion)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Criterion with ID %s not found", idCriterion)));

    return CriterionConverter.toInfo(criterionEntityFromRequest);
  }

  /**
   * Возвращает множество всех полных критерий - с типом критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  @Override
  public Set<CriterionInfo> readAllFull() throws NotFoundException {

    Set<CriterionInfo> criterionInfoSet = criterionRepository
        .findAllByCriterionTypeIsNotNull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());

    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    return criterionInfoSet;
  }

  /**
   * Возвращает множество всех неполных критерий - без типа критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  @Override
  public Set<CriterionInfo> readAllPartial() throws NotFoundException {

    Set<CriterionInfo> criterionInfoSet = criterionRepository
        .findAllByCriterionTypeIsNull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());

    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    return criterionInfoSet;
  }

  /**
   * Возваращает дополнительные данные для критерия.
   * Данные содержат в себе множетсво кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public CriterionInitData getInitData() throws NotFoundException {

    // Получение множества кратких информаций о типах критерий
    Set<ObjectShortInfo> criterionTypeShortInfoSet = criterionTypeRepository
        .findAll()
        .stream()
        .map(CriterionTypeConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (criterionTypeShortInfoSet.isEmpty()) {
      throw new NotFoundException("Criterion types not found");
    }

    return CriterionInitData
        .builder()
        .shortCriterionTypeSet(criterionTypeShortInfoSet)
        .build();
  }
}
