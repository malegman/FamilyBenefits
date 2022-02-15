package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionSave;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.convert.CriterionTypeDBConverter;
import com.example.familybenefits.dto.entity.BenefitEntity;
import com.example.familybenefits.dto.entity.CriterionEntity;
import com.example.familybenefits.dto.entity.CriterionTypeEntity;
import com.example.familybenefits.dto.entity.UserEntity;
import com.example.familybenefits.dto.repository.CriterionRepository;
import com.example.familybenefits.dto.repository.CriterionTypeRepository;
import com.example.familybenefits.dto.repository.UserRepository;
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
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

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
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param criterionTypeDBService интерфейс сервиса модели таблицы "criterion_type", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository,
                            UserRepository userRepository,
                            EntityDBService<CriterionTypeEntity, CriterionTypeRepository> criterionTypeDBService,
                            DBIntegrityService dbIntegrityService) {
    this.criterionRepository = criterionRepository;
    this.userRepository = userRepository;
    this.criterionTypeDBService = criterionTypeDBService;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Возвращает множество критерий, в которых есть пособия.
   * Фильтр по пособию и типу критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return множество кратких информаций о критериях
   */
  @Override
  public Set<ObjectShortInfo> readAllFilter(String idBenefit, String idCriterionType) {

    return findAllFull()
        .stream()
        .filter(criterionEntity ->
                    (idBenefit == null || criterionEntity.getBenefitEntitySet()
                        .stream()
                        .map(BenefitEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idBenefit)
                    ) && (idCriterionType == null || idCriterionType.equals(criterionEntity.getCriterionTypeEntity().getId())))
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Создает критерий по запросу на сохранение
   * @param criterionSave объект запроса для сохранения критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия данного критерия с указанным ID не найден
   */
  @Override
  public void create(CriterionSave criterionSave) throws AlreadyExistsException, NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromSave = CriterionDBConverter
        .fromSave(criterionSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeDBService.getRepository()::existsById, criterionEntityFromSave.getCriterionTypeEntity());

    // Проверка отсутствия критерия по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        criterionRepository::existsByName, criterionEntityFromSave.getName());

    criterionRepository.saveAndFlush(criterionEntityFromSave);
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
   * Обновляет данные критерия по запросу на сохранение
   * @param idCriterion ID критерия
   * @param criterionSave объект запроса для сохранения критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   * @throws AlreadyExistsException если критерий с отличным ID и данным названием уже существует
   */
  @Override
  public void update(String idCriterion, CriterionSave criterionSave) throws NotFoundException, AlreadyExistsException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromSave = CriterionDBConverter
        .fromSave(criterionSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования типа критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionTypeDBService.getRepository()::existsById, criterionEntityFromSave.getCriterionTypeEntity());

    String prepareIdCriterion = dbIntegrityService.preparePostgreSQLString(idCriterion);

    // Проверка существования критерия по его ID
    dbIntegrityService.checkExistenceById(
        criterionRepository::existsById, prepareIdCriterion);

    // Проверка отсутствия критерия с отличным от данного ID и данным названием
    dbIntegrityService.checkAbsenceAnotherByUniqStr(
        criterionRepository::existsByIdIsNotAndName, prepareIdCriterion, criterionEntityFromSave.getName());

    criterionEntityFromSave.setId(prepareIdCriterion);

    criterionRepository.saveAndFlush(criterionEntityFromSave);
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
   * Возвращает множество критерий, в которых нет пособий
   * @return множество кратких информаций о критериях
   */
  @Override
  public Set<ObjectShortInfo> readAllPartial() {

    return findAllPartial()
        .stream()
        .map(CriterionDBConverter::toShortInfo)
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
   * Возвращает подобранные пользователю пособия
   * @param idUser ID пользователя
   * @return множество кратких информаций о пособиях
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @Override
  public Set<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException {

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    return userEntityFromRequest.getCriterionEntitySet()
        .stream()
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toSet());
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
