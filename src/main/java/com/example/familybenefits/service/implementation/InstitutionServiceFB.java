package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.InstitutionService;
import com.example.familybenefits.service.s_interface.PartEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "учреждение"
 */
@Service
public class InstitutionServiceFB implements InstitutionService, PartEntityService<InstitutionEntity> {

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CityEntity> cityPartEntityService;
  /**
   * Интерфейс сервиса для моделей таблицы "benefit", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<BenefitEntity> benefitPartEntityService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param cityPartEntityService интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   * @param benefitPartEntityService интерфейс сервиса для моделей таблицы "benefit", целостность которых зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public InstitutionServiceFB(InstitutionRepository institutionRepository,
                              PartEntityService<CityEntity> cityPartEntityService,
                              @Lazy PartEntityService<BenefitEntity> benefitPartEntityService,
                              DBIntegrityService dbIntegrityService) {
    this.institutionRepository = institutionRepository;
    this.cityPartEntityService = cityPartEntityService;
    this.benefitPartEntityService = benefitPartEntityService;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет учреждение по запросу на добавление
   * @param institutionAdd объект запроса на добавление учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город учреждения с указанным ID не найден
   */
  @Override
  public void add(InstitutionAdd institutionAdd) throws AlreadyExistsException, NotFoundException {

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityPartEntityService::existsById, institutionAdd.getIdCity(), R.NAME_OBJECT_CITY);
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        benefitPartEntityService::existsById, institutionAdd.getIdBenefitSet(), R.NAME_OBJECT_BENEFIT);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromAdd = (InstitutionEntity) InstitutionConverter
        .fromAdd(institutionAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия учреждения по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        institutionRepository::existsByName, institutionEntityFromAdd.getName(), R.NAME_OBJECT_INSTITUTION);

    institutionRepository.saveAndFlush(institutionEntityFromAdd);
  }

  /**
   * Обновляет учреждение по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void update(InstitutionUpdate institutionUpdate) throws NotFoundException {

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityPartEntityService::existsById, institutionUpdate.getIdCity(), R.NAME_OBJECT_CITY);
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        benefitPartEntityService::existsById, institutionUpdate.getIdBenefitSet(), R.NAME_OBJECT_BENEFIT);

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionRepository::existsById, institutionUpdate.getId(), R.NAME_OBJECT_INSTITUTION);

    // Сохранение полученной модели таблицы из запроса с подготовленными строковыми значениями для БД
    institutionRepository.saveAndFlush((InstitutionEntity) InstitutionConverter
        .fromUpdate(institutionUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void delete(BigInteger idInstitution) throws NotFoundException {

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionRepository::existsById, idInstitution, R.NAME_OBJECT_INSTITUTION);

    institutionRepository.deleteById(idInstitution);
  }

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @Override
  public InstitutionInfo read(BigInteger idInstitution) throws NotFoundException {

    // Получение учреждения по его ID, если учреждение существует
    InstitutionEntity institutionEntityFromRequest = institutionRepository.findById(idInstitution)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Institution with ID \"%s\" not found", idInstitution)));

    return InstitutionConverter.toInfo(institutionEntityFromRequest);
  }

  /**
   * Возвращает множество учреждений, в которых есть пособия
   * @return множество информаций об учреждениях
   */
  @Override
  public Set<InstitutionInfo> getAll() {

    return findAllFull()
        .stream()
        .map(InstitutionConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество учреждений, в которых нет пособий
   * @return множество информаций об учреждениях
   */
  @Override
  public Set<InstitutionInfo> getAllPartial() {

    return findAllPartial()
        .stream()
        .map(InstitutionConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе множество кратких информаций о городах.
   * @return дополнительные данные для учреждения
   */
  @Override
  public InstitutionInitData getInitData() {

    return InstitutionInitData
        .builder()
        .shortCitySet(cityPartEntityService
                          .findAllFull()
                          .stream()
                          .map(CityConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .shortBenefitSet(benefitPartEntityService
                             .findAllFull()
                             .stream()
                             .map(BenefitConverter::toShortInfo)
                             .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Проверяет существование модели таблицы "institution" по ID
   * @param id ID модели
   * @return true, если модель существует
   */
  @Override
  public boolean existsById(BigInteger id) {

    return institutionRepository.existsById(id);
  }


  /**
   * Возвращает множество моделей таблицы "institution", в которых есть модели пособий
   * @return множество моделей таблиц
   */
  @Override
  public Set<InstitutionEntity> findAllFull() {

    return institutionRepository
        .findAll()
        .stream()
        .filter(institutionEntity -> !institutionEntity.getBenefitEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "institution", в которых нет моделей пособий
   * @return множество моделей таблиц
   */
  @Override
  public Set<InstitutionEntity> findAllPartial() {

    return institutionRepository
        .findAll()
        .stream()
        .filter(institutionEntity -> institutionEntity.getBenefitEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
