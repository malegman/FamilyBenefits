package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.convert.CriterionTypeDBConverter;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.CriterionTypeEntity;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CriterionService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "критерий"
 */
@Service
public class CriterionServiceFB implements CriterionService, EntityDBService<CriterionEntity, CriterionRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Интерфейс сервиса модели таблицы "criterion_type", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CriterionTypeEntity, CriterionTypeRepository> criterionTypeDBService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param criterionTypeDBService интерфейс сервиса модели таблицы "criterion_type", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository,
                            EntityDBService<CriterionTypeEntity, CriterionTypeRepository> criterionTypeDBService,
                            DBIntegrityService dbIntegrityService) {
    this.criterionRepository = criterionRepository;
    this.criterionTypeDBService = criterionTypeDBService;
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

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromAdd = CriterionDBConverter
        .fromAdd(criterionAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeDBService.getRepository()::existsById, criterionEntityFromAdd.getCriterionTypeEntity());

    // Проверка отсутствия критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        criterionRepository::existsByName, criterionEntityFromAdd.getName());

    criterionRepository.saveAndFlush(criterionEntityFromAdd);
  }

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   */
  @Override
  public void update(CriterionUpdate criterionUpdate) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromUpdate = CriterionDBConverter
        .fromUpdate(criterionUpdate, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeDBService.getRepository()::existsById, criterionEntityFromUpdate.getCriterionTypeEntity());

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionRepository::existsById, criterionEntityFromUpdate);

    criterionRepository.saveAndFlush(criterionEntityFromUpdate);
  }

  /**
   * Удаляет критерий по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public void delete(String idCriterion) throws NotFoundException {

    String prepareIdCriterion = dbIntegrityService.preparePostgreSQLString(idCriterion);

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionRepository::existsById, prepareIdCriterion);

    criterionRepository.deleteById(prepareIdCriterion);
  }

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public CriterionInfo read(String idCriterion) throws NotFoundException {

    String prepareIdCriterion = dbIntegrityService.preparePostgreSQLString(idCriterion);

    // Получение критерия по его ID, если критерий существует
    CriterionEntity criterionEntityFromRequest = criterionRepository.findById(prepareIdCriterion)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Criterion with ID \"%s\" not found", idCriterion)));

    return CriterionDBConverter.toInfo(criterionEntityFromRequest);
  }

  /**
   * Возвращает множество критерий, в которых есть пособия
   * @return множество информаций о критериях
   */
  @Override
  public Set<CriterionInfo> getAll() {

    return findAllFull()
        .stream()
        .map(CriterionDBConverter::toInfo)
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
        .map(CriterionDBConverter::toInfo)
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
        .shortCriterionTypeSet(criterionTypeDBService
                                   .findAllFull()
                                   .stream()
                                   .map(CriterionTypeDBConverter::toShortInfo)
                                   .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  @Override
  public CriterionRepository getRepository() {
    return criterionRepository;
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
