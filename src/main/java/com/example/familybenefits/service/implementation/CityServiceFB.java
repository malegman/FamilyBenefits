package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityInitData;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.CityService;
import com.example.familybenefits.service.s_interface.PartEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "город"
 */
@Service
public class CityServiceFB implements CityService, PartEntityService<CityEntity> {

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

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
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param benefitPartEntityService интерфейс сервиса для моделей таблицы "benefit", целостность которых зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public CityServiceFB(CityRepository cityRepository,
                       @Lazy PartEntityService<BenefitEntity> benefitPartEntityService,
                       DBIntegrityService dbIntegrityService) {
    this.cityRepository = cityRepository;
    this.benefitPartEntityService = benefitPartEntityService;
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
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, cityAdd.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromAdd = (CityEntity) CityConverter
        .fromAdd(cityAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка отсутствия города по его названию
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
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
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityRepository::existsById, cityUpdate.getIdBenefitSet(),
        "Benefit with ID %s not found");

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
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
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
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

    // Получение города по его ID, если город существует
    CityEntity cityEntityFromRequest = cityRepository.findById(idCity)
        .orElseThrow(() -> new NotFoundException(String.format(
            "City with ID %s not found", idCity)));

    return CityConverter.toInfo(cityEntityFromRequest);
  }

  /**
   * Возвращает множество городов, в которых есть учреждения и пособия
   * @return множество информаций о городах
   */
  @Override
  public Set<CityInfo> getAll() {

    return findAllFull()
        .stream()
        .map(CityConverter::toInfo)
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
        .map(CityConverter::toInfo)
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
        .shortBenefitSet(benefitPartEntityService
                             .findAllFull()
                             .stream()
                             .map(BenefitConverter::toShortInfo)
                             .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Проверяет существование модели таблицы "city" по ID
   * @param id ID модели
   * @return true, если модель существует
   */
  @Override
  public boolean existsById(BigInteger id) {

    return cityRepository.existsById(id);
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
