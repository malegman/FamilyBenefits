package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.convert.BenefitDBConverter;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.convert.InstitutionDBConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import com.example.familybenefits.service.s_interface.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "учреждение"
 */
@Service
public class InstitutionServiceFB implements InstitutionService, EntityDBService<InstitutionEntity, InstitutionRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CityEntity, CityRepository> cityDBService;
  /**
   * Интерфейс сервиса модели таблицы "benefit", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<BenefitEntity, BenefitRepository> benefitDBService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param cityDBService интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   * @param benefitDBService интерфейс сервиса модели таблицы "benefit", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public InstitutionServiceFB(InstitutionRepository institutionRepository,
                              EntityDBService<CityEntity, CityRepository> cityDBService,
                              @Lazy EntityDBService<BenefitEntity, BenefitRepository> benefitDBService,
                              DBIntegrityService dbIntegrityService) {
    this.institutionRepository = institutionRepository;
    this.cityDBService = cityDBService;
    this.benefitDBService = benefitDBService;
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

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromAdd = InstitutionDBConverter
        .fromAdd(institutionAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, institutionEntityFromAdd.getCityEntity());
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, institutionEntityFromAdd.getBenefitEntitySet());

    // Проверка отсутствия учреждения по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        institutionRepository::existsByName, institutionEntityFromAdd.getName());

    institutionRepository.saveAndFlush(institutionEntityFromAdd);
  }

  /**
   * Обновляет учреждение по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void update(InstitutionUpdate institutionUpdate) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromUpdate = InstitutionDBConverter
        .fromUpdate(institutionUpdate, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, institutionEntityFromUpdate.getCityEntity());
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, institutionEntityFromUpdate.getBenefitEntitySet());

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceById(
        institutionRepository::existsById, institutionEntityFromUpdate);

    institutionRepository.saveAndFlush(institutionEntityFromUpdate);
  }

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void delete(String idInstitution) throws NotFoundException {

    String prepareIdInstitution = dbIntegrityService.preparePostgreSQLString(idInstitution);

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceById(
        institutionRepository::existsById, prepareIdInstitution);

    institutionRepository.deleteById(prepareIdInstitution);
  }

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @Override
  public InstitutionInfo read(String idInstitution) throws NotFoundException {

    String prepareIdInstitution = dbIntegrityService.preparePostgreSQLString(idInstitution);

    // Получение учреждения по его ID, если учреждение существует
    InstitutionEntity institutionEntityFromRequest = institutionRepository.findById(prepareIdInstitution)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Institution with ID \"%s\" not found", idInstitution)));

    return InstitutionDBConverter.toInfo(institutionEntityFromRequest);
  }

  /**
   * Возвращает множество учреждений, в которых есть пособия
   * @return множество информаций об учреждениях
   */
  @Override
  public Set<InstitutionInfo> getAll() {

    return findAllFull()
        .stream()
        .map(InstitutionDBConverter::toInfo)
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
        .map(InstitutionDBConverter::toInfo)
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
        .shortCitySet(cityDBService
                          .findAllFull()
                          .stream()
                          .map(CityDBConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .shortBenefitSet(benefitDBService
                             .findAllFull()
                             .stream()
                             .map(BenefitDBConverter::toShortInfo)
                             .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  @Override
  public InstitutionRepository getRepository() {
    return institutionRepository;
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
