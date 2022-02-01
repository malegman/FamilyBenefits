package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.BenefitService;
import com.example.familybenefits.service.s_interface.PartEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService, PartEntityService<BenefitEntity> {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CityEntity> cityPartEntityService;

  /**
   * Интерфейс сервиса для моделей таблицы "institution", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<InstitutionEntity> institutionPartEntityService;

  /**
   * Интерфейс сервиса для моделей таблицы "criterion", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CriterionEntity> criterionPartEntityService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param cityPartEntityService интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   * @param institutionPartEntityService интерфейс сервиса для моделей таблицы "institution", целостность которых зависит от связанных таблиц
   * @param criterionPartEntityService интерфейс сервиса для моделей таблицы "criterion", целостность которых зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository,
                          PartEntityService<CityEntity> cityPartEntityService,
                          PartEntityService<InstitutionEntity> institutionPartEntityService,
                          PartEntityService<CriterionEntity> criterionPartEntityService,
                          DBIntegrityService dbIntegrityService) {
    this.benefitRepository = benefitRepository;
    this.cityPartEntityService = cityPartEntityService;
    this.institutionPartEntityService = institutionPartEntityService;
    this.criterionPartEntityService = criterionPartEntityService;
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
        cityPartEntityService::existsById, benefitAdd.getIdCitySet(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionPartEntityService::existsById, benefitAdd.getIdCriterionSet(),
        "Criterion with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionPartEntityService::existsById, benefitAdd.getIdInstitutionSet(),
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
        cityPartEntityService::existsById, benefitUpdate.getIdCitySet(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionPartEntityService::existsById, benefitUpdate.getIdCriterionSet(),
        "Criterion with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        institutionPartEntityService::existsById, benefitUpdate.getIdInstitutionSet(),
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
   * Возвращает множество пособий, в которых есть города, учреждения и критерии
   * @return множество информаций о пособиях
   */
  @Override
  public Set<BenefitInfo> getAll() {

    return findAllFull()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество пособий, в которых нет городов, учреждений или критерий
   * @return множество информаций о пособиях
   */
  @Override
  public Set<BenefitInfo> getAllPartial() {

    return findAllPartial()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для пособия
   */
  @Override
  public BenefitInitData getInitData() {

    return BenefitInitData
        .builder()
        .shortCitySet(cityPartEntityService
                          .findAllFull()
                          .stream()
                          .map(CityConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .criterionSet(criterionPartEntityService
                          .findAllFull()
                          .stream()
                          .map(CriterionConverter::toInfo)
                          .collect(Collectors.toSet()))
        .shortInstitutionSet(institutionPartEntityService
                                 .findAllFull()
                                 .stream()
                                 .map(InstitutionConverter::toShortInfo)
                                 .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Проверяет существование модели таблицы "benefit" по ID
   * @param id ID модели
   * @return true, если модель существует
   */
  @Override
  public boolean existsById(BigInteger id) {

    return benefitRepository.existsById(id);
  }

  /**
   * Возвращает множество моделей таблицы "benefit", в которых есть модели городов, учреждений и критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<BenefitEntity> findAllFull() {

    return benefitRepository
        .findAll()
        .stream()
        .filter(be -> !be.getCityEntitySet().isEmpty()
            && !be.getInstitutionEntitySet().isEmpty()
            && !be.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "benefit", в которых нет моделей городов, учреждений и критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<BenefitEntity> findAllPartial() {

    return benefitRepository
        .findAll()
        .stream()
        .filter(be -> be.getCityEntitySet().isEmpty()
            || be.getInstitutionEntitySet().isEmpty()
            || be.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
