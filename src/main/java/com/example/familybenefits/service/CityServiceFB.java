package com.example.familybenefits.service;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityInitData;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.DBIntegrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "город"
 */
@Service
public class CityServiceFB implements CityService {

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
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CityServiceFB(CityRepository cityRepository, BenefitRepository benefitRepository,
                       DBIntegrityService dbIntegrityService) {
    this.cityRepository = cityRepository;
    this.benefitRepository = benefitRepository;
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

    // Проверка существования пособий их ID
    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, cityAdd.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromAdd = (CityEntity) CityConverter
        .fromAdd(cityAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия города по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrow(
        cityRepository::existsByName, cityEntityFromAdd.getName(),
        "The city with name %s already exists");

    cityRepository.saveAndFlush(cityEntityFromAdd);
  }

  /**
   * Обновляет город по запросу на обновление
   * @param cityUpdate объект запроса на обновление города
   * @throws NotFoundException если город с указанными данными не найден
   */
  @Override
  public void update(CityUpdate cityUpdate) throws NotFoundException {

    // Проверка существования пособий их ID
    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, cityUpdate.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, cityUpdate.getId(),
        "City with ID %s not found");

    // Сохранение полученной модели таблицы из запроса с подготовленными строковыми значениями для БД
    cityRepository.saveAndFlush((CityEntity) CityConverter
        .fromUpdate(cityUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString));
  }

  /**
   * Удаляет город по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idCity) throws NotFoundException {

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceByIdElseThrow(
        cityRepository::existsById, idCity,
        "City with ID %s not found");

    cityRepository.deleteById(idCity);
  }

  /**
   * Возвращает информацию о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public CityInfo read(BigInteger idCity) throws NotFoundException {

    Optional<CityEntity> optCityEntity = cityRepository.findById(idCity);

    if (optCityEntity.isEmpty()) {
      throw new NotFoundException(String.format(
          "City with ID %s not found", idCity));
    }

    return CityConverter.toInfo(optCityEntity.get());
  }

  /**
   * Возвращает множество всех городов
   * @return множество информаций о городах
   * @throws NotFoundException если города не найдены
   */
  @Override
  public Set<CityInfo> readAll() throws NotFoundException {

    Set<CityInfo> cityInfoSet = cityRepository
        .findAll()
        .stream()
        .map(CityConverter::toInfo)
        .collect(Collectors.toSet());

    if (cityInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    return cityInfoSet;
  }

  /**
   * Возвращает дополнительные данные для города.
   * Данные содержат в себе множества кратких информаций о пособиях
   * @return дополнительные данные для города
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public CityInitData getInitData() throws NotFoundException {

    // Получение множества кратких информаций о всех полных пособиях
    Set<ObjectShortInfo> benefitShortInfoSet = benefitRepository
        .findAllFull()
        .stream()
        .map(BenefitConverter::toShortInfo)
        .collect(Collectors.toSet());

    if (benefitShortInfoSet.isEmpty()) {
      throw new NotFoundException("Benefits not found");
    }

    return CityInitData
        .builder()
        .shortBenefitSet(benefitShortInfoSet)
        .build();
  }
}
