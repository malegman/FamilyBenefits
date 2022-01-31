package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "учреждение"
 */
@Service
public class InstitutionServiceFB implements InstitutionService {

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public InstitutionServiceFB(InstitutionRepository institutionRepository, CityRepository cityRepository,
                              BenefitRepository benefitRepository, DBIntegrityService dbIntegrityService) {
    this.institutionRepository = institutionRepository;
    this.cityRepository = cityRepository;
    this.benefitRepository = benefitRepository;
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
        cityRepository::existsById, institutionAdd.getIdCity(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, institutionAdd.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromAdd = (InstitutionEntity) InstitutionConverter
        .fromAdd(institutionAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия учреждения по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        institutionRepository::existsByName, institutionEntityFromAdd.getName(),
        "The institution %s already exists");

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
        cityRepository::existsById, institutionUpdate.getIdCity(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, institutionUpdate.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionRepository::existsById, institutionUpdate.getId(),
        "Institution with ID %s not found");

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
        institutionRepository::existsById, idInstitution,
        "Institution with ID %s not found");

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
            "Institution with ID %s not found", idInstitution)));

    return InstitutionConverter.toInfo(institutionEntityFromRequest);
  }

  /**
   * Возвращает множество всех учреждений
   * @return множество информаций об учреждениях
   * @throws NotFoundException если учреждения не найдены
   */
  @Override
  public Set<InstitutionInfo> readAll() throws NotFoundException {

    Set<InstitutionInfo> institutionInfoSet = institutionRepository
        .findAll()
        .stream()
        .map(InstitutionConverter::toInfo)
        .collect(Collectors.toSet());

    if (institutionInfoSet.isEmpty()) {
      throw new NotFoundException("Institutions not found");
    }

    return institutionInfoSet;
  }

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе множество кратких информаций о городах.
   * @return дополнительные данные для учреждения
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public InstitutionInitData getInitData() throws NotFoundException {

    // Получение множества кратких информаций о всех городах
    Set<ObjectShortInfo> cityShortInfoSet = cityRepository
        .findAll()
        .stream()
        .map(CityConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (cityShortInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    // Получение множества кратких информаций о всех полных пособиях
    Set<ObjectShortInfo> benefitShortInfoSet = benefitRepository
        .findAllFull()
        .stream()
        .map(BenefitConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (benefitShortInfoSet.isEmpty()) {
      throw new NotFoundException("Benefits not found");
    }

    return InstitutionInitData
        .builder()
        .shortCitySet(cityShortInfoSet)
        .shortBenefitSet(benefitShortInfoSet)
        .build();
  }
}
