package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CitySave;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.dto.entity.BenefitEntity;
import com.example.familybenefits.dto.entity.CityEntity;
import com.example.familybenefits.dto.repository.BenefitRepository;
import com.example.familybenefits.dto.repository.CityRepository;
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
   * Возвращает множество городов, в которых есть учреждения и пособия.
   * Фильтр по названию города и пособию.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param nameCity Название города
   * @param idBenefit ID пособия
   * @return множество кратких информаций о городах
   */
  @Override
  public Set<ObjectShortInfo> readAllFilter(String nameCity, String idBenefit) {

    return findAllFull()
        .stream()
        .filter(cityEntity ->
                    (nameCity == null || nameCity.equals(cityEntity.getName())
                    ) && (idBenefit == null || cityEntity.getBenefitEntitySet()
                        .stream()
                        .map(BenefitEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idBenefit)))
        .map(CityDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Создает город по запросу на сохранение
   * @param citySave объект запроса на сохранение города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws NotFoundException если пособие города с указанным ID не найдено
   */
  @Override
  public void create(CitySave citySave) throws AlreadyExistsException, NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromSave = CityDBConverter
        .fromSave(citySave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования пособий их ID
    dbIntegrityService.checkExistenceById(
        benefitDBService.getRepository()::existsById, cityEntityFromSave.getBenefitEntitySet());

    // Проверка отсутствия города по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        cityRepository::existsByName, cityEntityFromSave.getName());

    cityRepository.saveAndFlush(cityEntityFromSave);
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
   * Обновляет город по запросу на сохранение
   * @param idCity ID города
   * @param citySave объект запроса на сохранение города
   * @throws NotFoundException если город с указанным ID не найден
   * @throws AlreadyExistsException если город с отличным ID и данным названием уже существует
   */
  @Override
  public void update(String idCity, CitySave citySave) throws NotFoundException, AlreadyExistsException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromSave = CityDBConverter
        .fromSave(citySave, dbIntegrityService::preparePostgreSQLString);

    String prepareIdCity = dbIntegrityService.preparePostgreSQLString(idCity);

    // Проверка существование города по его ID
    dbIntegrityService.checkExistenceById(
        cityRepository::existsById, prepareIdCity);

    // Проверка отсутствия города с отличным от данного ID и данным названием
    dbIntegrityService.checkAbsenceAnotherByUniqStr(
        cityRepository::existsByIdIsNotAndName, prepareIdCity, cityEntityFromSave.getName());

    cityEntityFromSave.setId(prepareIdCity);

    cityRepository.saveAndFlush(cityEntityFromSave);
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
   * Возвращает множество городов, в которых нет учреждений или пособий
   * @return множество кратких информаций о городах
   */
  @Override
  public Set<ObjectShortInfo> readAllPartial() {

    return findAllPartial()
        .stream()
        .map(CityDBConverter::toShortInfo)
        .collect(Collectors.toSet());
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
