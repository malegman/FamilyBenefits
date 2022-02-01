package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.CriterionTypeConverter;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CriterionService;
import com.example.familybenefits.service.s_interface.PartEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "критерий"
 */
@Service
public class CriterionServiceFB implements CriterionService, PartEntityService<CriterionEntity> {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Интерфейс сервиса для моделей таблицы "criterion_type", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CriterionTypeEntity> criterionTypePartEntityService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param criterionTypePartEntityService интерфейс сервиса для моделей таблицы "criterion_type", целостность которых зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository,
                            PartEntityService<CriterionTypeEntity> criterionTypePartEntityService,
                            DBIntegrityService dbIntegrityService) {
    this.criterionRepository = criterionRepository;
    this.criterionTypePartEntityService = criterionTypePartEntityService;
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
        criterionTypePartEntityService::existsById, criterionAdd.getIdCriterionType(),
        "Criterion type with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromAdd = (CriterionEntity) CriterionConverter
        .fromAdd(criterionAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        criterionRepository::existsByName, criterionEntityFromAdd.getName(),
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
        criterionTypePartEntityService::existsById, criterionUpdate.getIdCriterionType(),
        "Criterion type with ID %s not found");

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionRepository::existsById, criterionUpdate.getId(),
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
        criterionRepository::existsById, idCriterion,
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
   * Возвращает множество критерий, в которых есть пособия
   * @return множество информаций о критериях
   */
  @Override
  public Set<CriterionInfo> getAll() {

    return findAllFull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество критерий, в которых нет пособий
   * @return множество информаций о критериях
   */
  @Override
  public Set<CriterionInfo> getAllPartial() {

    return findAllPartial()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе множество кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   */
  @Override
  public CriterionInitData getInitData() {

    return CriterionInitData
        .builder()
        .shortCriterionTypeSet(criterionTypePartEntityService
                                   .findAllFull()
                                   .stream()
                                   .map(CriterionTypeConverter::toShortInfo)
                                   .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Проверяет существование модели таблицы "criterion" по ID
   * @param id ID модели
   * @return true, если модель существует
   */
  @Override
  public boolean existsById(BigInteger id) {

    return criterionRepository.existsById(id);
  }

  /**
   * Возвращает множество моделей таблицы "criterion", в которых есть модели пособий
   * @return множество моделей таблиц
   */
  @Override
  public Set<CriterionEntity> findAllFull() {

    return criterionRepository
        .findAll()
        .stream()
        .filter(criterionEntity -> !criterionEntity.getBenefitEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "criterion", в которых нет моделей пособий
   * @return множество моделей таблиц
   */
  @Override
  public Set<CriterionEntity> findAllPartial() {

    return criterionRepository
        .findAll()
        .stream()
        .filter(criterionEntity -> criterionEntity.getBenefitEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
