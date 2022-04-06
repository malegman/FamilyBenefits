package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.CityEntity;
import com.example.familybenefits.dto.repositories.CityRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.city.CityInfo;
import com.example.familybenefits.part_res_rest_api.api_model.city.CitySave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.converters.CityDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CityService;
import com.example.familybenefits.security.DBSecuritySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   */
  @Autowired
  public CityServiceFB(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  /**
   * Возвращает список городов, в которых есть учреждения и пособия.
   * Фильтр по названию, ID пособия или учреждения.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название города
   * @param idBenefit ID пособия
   * @param idInstitution ID учреждения
   * @return список кратких информаций о городах
   */
  @Override
  public List<ObjectShortInfo> readAllFilter(String name, String idBenefit, String idInstitution) {

    String preparedName = DBSecuritySupport.preparePostgreSQLString(name);
    String preparedIdBenefit = DBSecuritySupport.preparePostgreSQLString(idBenefit);
    String preparedIdInstitution = DBSecuritySupport.preparePostgreSQLString(idInstitution);

    return cityRepository.findAllFilter(preparedName, preparedIdBenefit, preparedIdInstitution)
        .stream()
        .map(CityDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Создает город по запросу на сохранение
   * @param citySave объект запроса на сохранение города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(CitySave citySave) throws AlreadyExistsException, InvalidStringException {

    // Проверка отсутствия города по его названию
    DBSecuritySupport.checkAbsenceByUniqStr(cityRepository::existsByName, citySave.getName());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    cityRepository.saveAndFlush(
        CityDBConverter.fromSave(null, citySave, DBSecuritySupport::preparePostgreSQLString));
  }

  /**
   * Возвращает информацию о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public CityInfo read(String idCity) throws NotFoundException {

    // Получение города по его ID, если город существует
    String preparedIdCity = DBSecuritySupport.preparePostgreSQLString(idCity);
    CityEntity cityEntityFromRequest = cityRepository.findById(preparedIdCity).orElseThrow(
        () -> new NotFoundException(String.format("City with ID \"%s\" not found", idCity)));

    return CityDBConverter.toInfo(cityEntityFromRequest);
  }

  /**
   * Обновляет город по запросу на сохранение
   * @param idCity ID города
   * @param citySave объект запроса на сохранение города
   * @throws NotFoundException если город с указанным ID не найден
   * @throws AlreadyExistsException если город с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idCity, CitySave citySave) throws NotFoundException, AlreadyExistsException, InvalidStringException {

    // Проверка отсутствия города с отличным от данного ID и данным названием
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        cityRepository::existsByIdIsNotAndName, idCity, citySave.getName());

    // Проверка существование города по его ID
    DBSecuritySupport.checkExistenceById(cityRepository::existsById, idCity);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CityEntity cityEntityFromSave = CityDBConverter
        .fromSave(idCity, citySave, DBSecuritySupport::preparePostgreSQLString);

    cityEntityFromSave.setId(idCity);

    cityRepository.saveAndFlush(cityEntityFromSave);
  }

  /**
   * Удаляет город по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public void delete(String idCity) throws NotFoundException {

    // Проверка существование города по его ID
    DBSecuritySupport.checkExistenceById(cityRepository::existsById, idCity);

    cityRepository.deleteById(idCity);
  }

  /**
   * Возвращает список городов, в которых нет учреждений или пособий
   * @return список кратких информаций о городах
   */
  @Override
  public List<ObjectShortInfo> readAllPartial() {

    return cityRepository.findAllPartial()
        .stream()
        .map(CityDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Проверяет существование города по его ID
   * @param idCity ID города, предварительно обработанный
   * @return true, если город найден
   */
  @Override
  public boolean existsById(String idCity) {

    return cityRepository.existsById(idCity);
  }

  /**
   * Возвращает список кратких информаций о городах, в которых есть пособия и учреждения
   * @return список кратких информаций о городах
   */
  @Override
  public List<ObjectShortInfo> readAllFullShort() {

    return cityRepository
        .findAllFilter(null, null, null)
        .stream()
        .map(CityDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Возвращает название города по его ID. Если город не найден, возвращается {@code null}
   * @param idCity ID города
   * @return название города, если город найден, иначе {@code null}
   */
  @Override
  public String readName(String idCity) {

    // Получение города по его ID, если город существует
    String preparedIdCity = DBSecuritySupport.preparePostgreSQLString(idCity);
    Optional<CityEntity> optCityEntityFromRequest = cityRepository.findById(preparedIdCity);

    return optCityEntityFromRequest.map(CityEntity::getName).orElse(null);
  }
}
