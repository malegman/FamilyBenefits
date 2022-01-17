package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.CityEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
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
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   */
  @Autowired
  public CityServiceFB(CityRepository cityRepository, InstitutionRepository institutionRepository, BenefitRepository benefitRepository) {
    this.cityRepository = cityRepository;
    this.institutionRepository = institutionRepository;
    this.benefitRepository = benefitRepository;
  }

  /**
   * Добавление города по запросу на добавление
   * @param cityAdd объект запроса на добавление города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   */
  @Override
  public void add(CityAdd cityAdd) throws AlreadyExistsException {

    CityEntity cityEntity = CityConverter.fromAdd(cityAdd);

    if (cityRepository.existsByName(cityEntity.getName())) {
      throw new AlreadyExistsException(String.format(
          "The city %s already exists", cityEntity.getName()
      ));
    }

    cityRepository.saveAndFlush(cityEntity);
  }

  /**
   * Обновление города по запросу на обновление
   * @param cityUpdate объект запроса на обновление города
   * @throws NotFoundException если город с указанными данными не найден
   */
  @Override
  public void update(CityUpdate cityUpdate) throws NotFoundException {

    CityEntity cityEntity = CityConverter.fromUpdate(cityUpdate);

    if (!cityRepository.existsById(cityEntity.getId())) {
      throw new NotFoundException(String.format(
          "City with ID %s not found", cityEntity.getId()
      ));
    }

    cityRepository.saveAndFlush(cityEntity);
  }

  /**
   * Удаление города по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idCity) throws NotFoundException {

    if (!cityRepository.existsById(idCity)) {
      throw new NotFoundException(String.format(
          "City with ID %s not found", idCity
      ));
    }

    cityRepository.deleteById(idCity);
  }

  /**
   * Получение информации о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  @Override
  public CityInfo read(BigInteger idCity) throws NotFoundException {

    return CityConverter.toInfo(cityRepository.findById(idCity)
            .orElseThrow(
                () -> new NotFoundException(String.format(
                    "City with ID %s not found", idCity
                )))
    );
  }

  /**
   * Получение множества всех существующих городов
   * @return множество информаций о городах
   * @throws NotFoundException если города не найдены
   */
  @Override
  public Set<CityInfo> readAll() throws NotFoundException {

    Set<CityInfo> cities = cityRepository.findAll()
        .stream()
        .map(CityConverter::toInfo)
        .collect(Collectors.toSet());
    if (cities.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    return cities;
  }

  /**
   * Получение множества всех учреждений города
   * @param idCity ID города
   * @return множество информаций о городах
   * @throws NotFoundException если учреждения не найдены или город с указынным ID не найден
   */
  @Override
  public Set<InstitutionInfo> readInstitutions(BigInteger idCity) throws NotFoundException {

    Optional<CityEntity> cityEntity = cityRepository.findById(idCity);
    if (cityEntity.isEmpty()) {
      throw new NotFoundException(String.format(
          "City with ID %s not found", idCity
      ));
    }

    Set<InstitutionInfo> institutions = institutionRepository.findAllByCityEntity(cityEntity.get())
        .stream()
        .map(InstitutionConverter::toInfo)
        .collect(Collectors.toSet());
    if (institutions.isEmpty()) {
      throw new NotFoundException(String.format(
          "Institutions of city with id %s not found", idCity
      ));
    }

    return institutions;
  }

  /**
   * Получение множества всех пособий города
   * @param idCity ID города
   * @return множество информаций о городах
   * @throws NotFoundException если пособия не найдены или город с указынным ID не найден
   */
  @Override
  public Set<BenefitInfo> readBenefits(BigInteger idCity) throws NotFoundException {

    Optional<CityEntity> cityEntity = cityRepository.findById(idCity);
    if (cityEntity.isEmpty()) {
      throw new NotFoundException(String.format(
          "City with ID %s not found", idCity
      ));
    }

    Set<BenefitInfo> benefits = benefitRepository.findAllByCityEntitySet(Collections.singleton(cityEntity.get()))
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
    if (benefits.isEmpty()) {
      throw new NotFoundException(String.format(
          "Benefits of city with id %s not found", idCity
      ));
    }

    return benefits;
  }
}
