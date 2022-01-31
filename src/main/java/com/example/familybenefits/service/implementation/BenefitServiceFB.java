package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository, CityRepository cityRepository,
                          InstitutionRepository institutionRepository, CriterionRepository criterionRepository,
                          DBIntegrityService dbIntegrityService) {
    this.benefitRepository = benefitRepository;
    this.cityRepository = cityRepository;
    this.institutionRepository = institutionRepository;
    this.criterionRepository = criterionRepository;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет новое пособие
   * @param benefitAdd объект запроса для добавления пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдено
   */
  @Override
  public void add(BenefitAdd benefitAdd) throws AlreadyExistsException, NotFoundException {

    // Проверка существования городов, критерий и учреждений по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, benefitAdd.getIdCitySet(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionRepository::existsById, benefitAdd.getIdCriterionSet(),
        "Criterion with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionRepository::existsById, benefitAdd.getIdInstitutionSet(),
        "Institution with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromAdd = (BenefitEntity) BenefitConverter
        .fromAdd(benefitAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия пособия по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        benefitRepository::existsByName, benefitEntityFromAdd.getName(),
        "The benefit with name %s already exists");

    benefitRepository.saveAndFlush(benefitEntityFromAdd);
  }

  /**
   * Обновляет пособие
   * @param benefitUpdate объект запроса для обновления пособия
   * @throws NotFoundException если пособие с указанными данными не найден
   */
  @Override
  public void update(BenefitUpdate benefitUpdate) throws NotFoundException {

    // Проверка существования городов, критерий и учреждений по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, benefitUpdate.getIdCitySet(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionRepository::existsById, benefitUpdate.getIdCriterionSet(),
        "Criterion with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionRepository::existsById, benefitUpdate.getIdInstitutionSet(),
        "Institution with ID %s not found");

    // Проверка существования пособия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        benefitRepository::existsById, benefitUpdate.getId(),
        "Benefit with ID %s not found");

    // Сохранение полученной модели таблицы из запроса с подготовленными строковыми значениями для БД
    benefitRepository.saveAndFlush((BenefitEntity) BenefitConverter
        .fromUpdate(benefitUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idBenefit) throws NotFoundException {

    // Проверка существование пособия по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        benefitRepository::existsById, idBenefit,
        "Benefit with ID %s not found");

    benefitRepository.deleteById(idBenefit);
  }

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public BenefitInfo read(BigInteger idBenefit) throws NotFoundException {

    // Получение пособия по его ID, если пособие существует
    BenefitEntity benefitEntityFromRequest = benefitRepository.findById(idBenefit)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Benefit with ID %s not found", idBenefit)));

    return BenefitConverter.toInfo(benefitEntityFromRequest);
  }

  /**
   * Возвращает множество всех полных пособий - с городом, учреждением и критерием
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  @Override
  public Set<BenefitInfo> readAllFull() throws NotFoundException {

    Set<BenefitInfo> benefitInfoSet = benefitRepository
        .findAllFull()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());

    if (benefitInfoSet.isEmpty()) {
      throw new NotFoundException("Benefits not found");
    }

    return benefitInfoSet;
  }

  /**
   * Возвращает множество всех неполных пособий - без города, учреждения или критерия
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  @Override
  public Set<BenefitInfo> readAllPartial() throws NotFoundException {

    Set<BenefitInfo> benefitInfoSet = benefitRepository
        .findAllPartial()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());

    if (benefitInfoSet.isEmpty()) {
      throw new NotFoundException("Benefits not found");
    }

    return benefitInfoSet;
  }

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для пособия
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public BenefitInitData getInitData() throws NotFoundException {

    // Получение множества кратких информаций о всех городах
    Set<ObjectShortInfo> cityShortInfoSet = cityRepository
        .findAll()
        .stream()
        .map(CityConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (cityShortInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    // Получение множества информаций о всех критериях с типом
    Set<CriterionInfo> criterionInfoSet = criterionRepository
        .findAllByCriterionTypeIsNotNull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());

    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    // Получение множества кратких информаций о всех учреждениях
    Set<ObjectShortInfo> institutionShortInfoSet = institutionRepository
        .findAll()
        .stream()
        .map(InstitutionConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (institutionShortInfoSet.isEmpty()) {
      throw new NotFoundException("Institutions not found");
    }

    return BenefitInitData
        .builder()
        .shortCitySet(cityShortInfoSet)
        .criterionSet(criterionInfoSet)
        .shortInstitutionSet(institutionShortInfoSet)
        .build();
  }
}
