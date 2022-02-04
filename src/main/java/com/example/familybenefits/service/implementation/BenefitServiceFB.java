package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.convert.BenefitDBConverter;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.convert.InstitutionDBConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.entity.InstitutionEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.BenefitService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService, EntityDBService<BenefitEntity, BenefitRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CityEntity, CityRepository> cityDBService;
  /**
   * Интерфейс сервиса модели таблицы "institution", целостность которых зависит от связанных таблиц
   */
  private final EntityDBService<InstitutionEntity, InstitutionRepository>institutionDBService;
  /**
   * Интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CriterionEntity, CriterionRepository> criterionDBService;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param cityDBService интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   * @param institutionDBService интерфейс сервиса модели таблицы "institution", целостность которой зависит от связанных таблиц
   * @param criterionDBService интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository,
                          @Lazy EntityDBService<CityEntity, CityRepository> cityDBService,
                          @Lazy EntityDBService<InstitutionEntity, InstitutionRepository> institutionDBService,
                          EntityDBService<CriterionEntity, CriterionRepository> criterionDBService,
                          DBIntegrityService dbIntegrityService) {
    this.benefitRepository = benefitRepository;
    this.cityDBService = cityDBService;
    this.institutionDBService = institutionDBService;
    this.criterionDBService = criterionDBService;
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

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromAdd = BenefitDBConverter
        .fromAdd(benefitAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования городов, критерий и учреждений по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, benefitEntityFromAdd.getCityEntitySet());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, benefitEntityFromAdd.getCriterionEntitySet());
    dbIntegrityService.checkExistenceById(
        institutionDBService.getRepository()::existsById, benefitEntityFromAdd.getInstitutionEntitySet());

    // Проверка отсутствия пособия по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        benefitRepository::existsByName, benefitEntityFromAdd.getName());

    benefitRepository.saveAndFlush(benefitEntityFromAdd);
  }

  /**
   * Обновляет пособие
   * @param benefitUpdate объект запроса для обновления пособия
   * @throws NotFoundException если пособие с указанными данными не найден
   */
  @Override
  public void update(BenefitUpdate benefitUpdate) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromUpdate = BenefitDBConverter
        .fromUpdate(benefitUpdate, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования городов, критерий и учреждений по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, benefitEntityFromUpdate.getCityEntitySet());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, benefitEntityFromUpdate.getCriterionEntitySet());
    dbIntegrityService.checkExistenceById(
        institutionDBService.getRepository()::existsById, benefitEntityFromUpdate.getInstitutionEntitySet());

    // Проверка существования пособия по его ID
    dbIntegrityService.checkExistenceById(
        benefitRepository::existsById, benefitEntityFromUpdate);

    benefitRepository.saveAndFlush(benefitEntityFromUpdate);
  }

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public void delete(String idBenefit) throws NotFoundException {

    String prepareIdBenefit = dbIntegrityService.preparePostgreSQLString(idBenefit);

    // Проверка существование пособия по его ID
    dbIntegrityService.checkExistenceById(
        benefitRepository::existsById, prepareIdBenefit);

    benefitRepository.deleteById(prepareIdBenefit);
  }

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public BenefitInfo read(String idBenefit) throws NotFoundException {

    String prepareIdBenefit = dbIntegrityService.preparePostgreSQLString(idBenefit);

    // Получение пособия по его ID, если пособие существует
    BenefitEntity benefitEntityFromRequest = benefitRepository.findById(prepareIdBenefit)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Benefit with ID \"%s\" not found", idBenefit)));

    return BenefitDBConverter.toInfo(benefitEntityFromRequest);
  }

  /**
   * Возвращает множество пособий, в которых есть города, учреждения и критерии
   * @return множество информаций о пособиях
   */
  @Override
  public Set<BenefitInfo> getAll() {

    return findAllFull()
        .stream()
        .map(BenefitDBConverter::toInfo)
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
        .map(BenefitDBConverter::toInfo)
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
        .shortCitySet(cityDBService
                          .findAllFull()
                          .stream()
                          .map(CityDBConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .criterionSet(criterionDBService
                          .findAllFull()
                          .stream()
                          .map(CriterionDBConverter::toInfo)
                          .collect(Collectors.toSet()))
        .shortInstitutionSet(institutionDBService
                                 .findAllFull()
                                 .stream()
                                 .map(InstitutionDBConverter::toShortInfo)
                                 .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  @Override
  public BenefitRepository getRepository() {
    return benefitRepository;
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
