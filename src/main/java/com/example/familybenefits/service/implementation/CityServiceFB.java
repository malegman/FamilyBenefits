package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityInitData;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.convert.BenefitDBConverter;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CityService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "город"
 */
@Service
public class CityServiceFB implements CityService, EntityDBService<CityEntity, CityRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

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
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param benefitDBService интерфейс сервиса модели таблицы "benefit", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CityServiceFB(CityRepository cityRepository,
                       @Lazy EntityDBService<BenefitEntity, BenefitRepository> benefitDBService,
                       DBIntegrityService dbIntegrityService) {
    this.cityRepository = cityRepository;
    this.benefitDBService = benefitDBService;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Добавляет город по запросу на добавление
   * @param cityAdd объект запроса на добавление города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws NotFoundException если пособие города с указанным ID не найдено
   */
  @Override
  public void add(CityAdd cityAdd) throws AlreadyExistsException, NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromAdd = CityDBConverter
        .fromAdd(cityAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования пособий их ID
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, cityEntityFromAdd.getBenefitEntitySet());

    // Проверка отсутствия города по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        cityRepository::existsByName, cityEntityFromAdd.getName());

    cityRepository.saveAndFlush(cityEntityFromAdd);
  }

  /**
   * Обновляет город по запросу на обновление
   * @param cityUpdate объект запроса на обновление города
   * @throws NotFoundException если город с указанными данными не найден
   */
  @Override
  public void update(CityUpdate cityUpdate) throws NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromUpdate = CityDBConverter
        .fromUpdate(cityUpdate, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования пособий их ID
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, cityEntityFromUpdate.getBenefitEntitySet());

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceById(
        cityRepository::existsById, cityEntityFromUpdate);

    cityRepository.saveAndFlush(cityEntityFromUpdate);
  }

  /**
   * Удаляет город по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public void delete(String idCity) throws NotFoundException {

    String prepareIdCity = dbIntegrityService.preparePostgreSQLString(idCity);

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceById(
        cityRepository::existsById, prepareIdCity);

    cityRepository.deleteById(prepareIdCity);
  }

  /**
   * Возвращает информацию о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public CityInfo read(String idCity) throws NotFoundException {

    String prepareIdCity = dbIntegrityService.preparePostgreSQLString(idCity);

    // Получение города по его ID, если город существует
    CityEntity cityEntityFromRequest = cityRepository.findById(prepareIdCity)
        .orElseThrow(() -> new NotFoundException(String.format(
            "City with ID \"%s\" not found", idCity)));

    return CityDBConverter.toInfo(cityEntityFromRequest);
  }

  /**
   * Возвращает множество городов, в которых есть учреждения и пособия
   * @return множество информаций о городах
   */
  @Override
  public Set<CityInfo> getAll() {

    return findAllFull()
        .stream()
        .map(CityDBConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество городов, в которых нет учреждений или пособий
   * @return множество информаций о городах
   */
  @Override
  public Set<CityInfo> getAllPartial() {

    return findAllPartial()
        .stream()
        .map(CityDBConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает дополнительные данные для города.
   * Данные содержат в себе множества кратких информаций о пособиях
   * @return дополнительные данные для города
   */
  @Override
  public CityInitData getInitData() {

    return CityInitData
        .builder()
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
  public CityRepository getRepository() {
    return cityRepository;
  }

  /**
   * Возвращает множество моделей таблицы "city", в которых есть модели пособий и учреждений
   * @return множество моделей таблиц
   */
  @Override
  public Set<CityEntity> findAllFull() {

    return cityRepository
        .findAll()
        .stream()
        .filter(cityEntity -> !cityEntity.getBenefitEntitySet().isEmpty()
            && !cityEntity.getInstitutionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "city", в которых нет моделей пособий или учреждений
   * @return множество моделей таблиц
   */
  @Override
  public Set<CityEntity> findAllPartial() {

    return cityRepository
        .findAll()
        .stream()
        .filter(cityEntity -> cityEntity.getBenefitEntitySet().isEmpty()
            || cityEntity.getInstitutionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
