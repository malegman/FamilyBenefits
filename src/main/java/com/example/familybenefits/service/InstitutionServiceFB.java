package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityInfo;
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

    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, institutionAdd.getIdCity(),
        "City with ID %s not found");

    dbIntegrityService.checkAbsenceByUniqStrElseThrow(
        institutionRepository::existsByName, institutionAdd.getName(),
        "The institution %s already exists");

    institutionRepository.saveAndFlush((InstitutionEntity) InstitutionConverter
        .fromAdd(institutionAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Обновляет учреждение по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void update(InstitutionUpdate institutionUpdate) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, institutionUpdate.getIdCity(),
        "City with ID %s not found");

    dbIntegrityService.checkExistenceByIdElseThrow(
        institutionRepository::existsById, institutionUpdate.getId(),
        "Institution with ID %s not found");

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

    dbIntegrityService.checkExistenceByIdElseThrow(
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

    dbIntegrityService.checkExistenceByIdElseThrow(
        institutionRepository::existsById, idInstitution,
        "Institution with ID %s not found");

    return InstitutionConverter.toInfo(institutionRepository.getById(idInstitution));
  }

  /**
   * Возваращает дополнительные данные для учреждения.
   * Данные содержат в себе множество кратких информаций о городах.
   * @return дополнительные данные для учреждения
   * @throws NotFoundException если данные не найдены
   */
  public InstitutionInitData getInitData() throws NotFoundException {

    Set<ObjectShortInfo> cityShortInfoSet = cityRepository
        .findAll()
        .stream()
        .map(CityConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (cityShortInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    return InstitutionInitData
        .builder()
        .shortCitySet(cityShortInfoSet)
        .build();
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
   * Возвращает информацию о городе учреждения
   * @param idInstitution ID учреждения
   * @return информация о городе учреждения
   * @throws NotFoundException если учреждение не найдено
   */
  public CityInfo readCity(BigInteger idInstitution) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        institutionRepository::existsById, idInstitution,
        "Institution with ID %s not found");

    return CityConverter.toInfo(institutionRepository.getById(idInstitution).getCityEntity());
  }

  /**
   * Возваращает множество пособий учреждения
   * @param idInstitution ID учреждения
   * @return множество пособий учреждений
   * @throws NotFoundException если пособия учреждения не найдены или учреждение не найдено
   */
  public Set<BenefitInfo> readBenefits(BigInteger idInstitution) throws NotFoundException {

    dbIntegrityService.checkExistenceByIdElseThrow(
        institutionRepository::existsById, idInstitution,
        "Institution with ID %s not found");

    Set<BenefitInfo> benefitInfoSet = benefitRepository
        .findAllFullWhereInstitutionIdEquals(idInstitution)
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
    if (benefitInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Benefits of institution with id %s not found", idInstitution));
    }

    return benefitInfoSet;
  }
}
