package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionSave;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
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
   * Возвращает множество учреждений, в которых есть пособия.
   * Фильтр по городу и пособию.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return множество кратких информаций об учреждениях
   */
  @Override
  public Set<ObjectShortInfo> readAllFilter(String idCity, String idBenefit) {

    return findAllFull()
        .stream()
        .filter(institutionEntity ->
                    (idCity == null || idCity.equals(institutionEntity.getCityEntity().getId())
                    ) && (idBenefit == null || institutionEntity.getBenefitEntitySet()
                        .stream()
                        .map(BenefitEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idBenefit)))
        .map(InstitutionDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Создает учреждение по запросу на сохранение
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город или пособия с указанными ID не найдены
   */
  @Override
  public void create(InstitutionSave institutionSave) throws AlreadyExistsException, NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromSave = InstitutionDBConverter
        .fromSave(institutionSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, institutionEntityFromSave.getCityEntity());
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, institutionEntityFromSave.getBenefitEntitySet());

    // Проверка отсутствия учреждения по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        institutionRepository::existsByName, institutionEntityFromSave.getName());

    institutionRepository.saveAndFlush(institutionEntityFromSave);
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
   * Обновляет учреждение по запросу на сохранение
   * @param idInstitution ID учреждения
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws NotFoundException если учреждение, город или пособия с указанными ID не найдены
   */
  @Override
  public void update(String idInstitution, InstitutionSave institutionSave) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromSave = InstitutionDBConverter
        .fromSave(institutionSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и пособий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, institutionEntityFromSave.getCityEntity());
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, institutionEntityFromSave.getBenefitEntitySet());

    // Проверка существования учреждения по его ID
    dbIntegrityService.checkExistenceById(
        institutionRepository::existsById, institutionEntityFromSave);

    institutionRepository.saveAndFlush(institutionEntityFromSave);
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
   * Возвращает множество учреждений, в которых нет пособий
   * @return множество кратких информаций об учреждениях
   */
  @Override
  public Set<ObjectShortInfo> readAllPartial() {

    return findAllPartial()
        .stream()
        .map(InstitutionDBConverter::toShortInfo)
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
