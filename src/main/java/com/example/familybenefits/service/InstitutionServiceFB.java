package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.InstitutionEntity;
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
 * Реализация сервиса, управляющего объектом "учреждение"
 */
@Service
public class InstitutionServiceFB implements InstitutionService {

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   */
  @Autowired
  public InstitutionServiceFB(InstitutionRepository institutionRepository, CityRepository cityRepository, BenefitRepository benefitRepository) {
    this.institutionRepository = institutionRepository;
    this.cityRepository = cityRepository;
    this.benefitRepository = benefitRepository;
  }

  /**
   * Добавляет учреждение по запросу на добавление
   * @param institutionAdd объект запроса на добавление учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   */
  @Override
  public void add(InstitutionAdd institutionAdd) throws AlreadyExistsException{

    InstitutionEntity institutionEntity = InstitutionConverter.fromAdd(institutionAdd);

    if (institutionRepository.existsByName(institutionEntity.getName())) {
      throw new AlreadyExistsException(String.format(
          "The institution %s already exists", institutionEntity.getName()
      ));
    }

    institutionRepository.saveAndFlush(institutionEntity);
  }

  /**
   * Обновляет учреждение по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void update(InstitutionUpdate institutionUpdate) throws NotFoundException {

    InstitutionEntity institutionEntity = InstitutionConverter.fromUpdate(institutionUpdate);

    if (!institutionRepository.existsById(institutionEntity.getId())) {
      throw new NotFoundException(String.format(
          "Institution with ID %s not found", institutionEntity.getId()
      ));
    }

    institutionRepository.saveAndFlush(institutionEntity);
  }

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void delete(BigInteger idInstitution) throws NotFoundException {

    if (!institutionRepository.existsById(idInstitution)) {
      throw new NotFoundException(String.format(
          "Institution with ID %s not found", idInstitution
      ));
    }

    institutionRepository.deleteById(idInstitution);
  }

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @Override
  public InstitutionInfo read(BigInteger idInstitution) throws NotFoundException {

    return InstitutionConverter
        .toInfo(institutionRepository
                    .findById(idInstitution)
                    .orElseThrow(
                        () -> new NotFoundException(String.format(
                            "Institution with ID %s not found", idInstitution
                        )))
    );
  }

  /**
   * Возваращает дополнительные данные для учреждения
   * @return дополнительные данные для учреждения
   * @throws NotFoundException если данные не найдены
   */
  public InstitutionInitData getInitData() throws NotFoundException {

    Set<ObjectShortInfo> cityShortInfoSet = cityRepository.findAll()
        .stream()
        .map(CityConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (cityShortInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    return InstitutionInitData
        .builder()
        .shortCitySet(cityShortInfoSet)
        .build();
  }

  /**
   * Возвращает информацию о городе учреждения
   * @param idInstitution ID учреждения
   * @return информация о городе учреждения
   * @throws NotFoundException если город не найден или учреждение не найдено
   */
  public CityInfo readCity(BigInteger idInstitution) throws NotFoundException {

    Optional<InstitutionEntity> optInstitutionEntity = institutionRepository.findById(idInstitution);
    if (optInstitutionEntity.isEmpty()) {
      throw new NotFoundException(String.format(
          "Institution with ID %s not found", idInstitution
      ));
    }

    return CityConverter.toInfo(optInstitutionEntity.get().getCityEntity());
  }

  /**
   * Возваращает множество пособий учреждения
   * @param idInstitution ID учреждения
   * @return множество пособий учреждений
   * @throws NotFoundException если пособия учреждения не найдены или учреждение не найдено
   */
  public Set<BenefitInfo> readBenefits(BigInteger idInstitution) throws NotFoundException {

    Optional<InstitutionEntity> optInstitutionEntity = institutionRepository.findById(idInstitution);
    if (optInstitutionEntity.isEmpty()) {
      throw new NotFoundException(String.format(
          "Institution with ID %s not found", idInstitution
      ));
    }

    Set<BenefitInfo> benefitInfoSet = benefitRepository.findAllByInstitutionEntitySet(Collections.singleton(optInstitutionEntity.get()))
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
    if (benefitInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Benefits of institution with id %s not found", idInstitution
      ));
    }

    return benefitInfoSet;
  }
}
