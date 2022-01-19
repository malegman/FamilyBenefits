package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.InstitutionConverter;
import com.example.familybenefits.dao.entity.BenefitEntity;
import com.example.familybenefits.dao.repository.BenefitRepository;
import com.example.familybenefits.dao.repository.CityRepository;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.InstitutionRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "city"
   */
  private final CityRepository cityRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param cityRepository репозиторий, работающий с моделью таблицы "city"
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository, CityRepository cityRepository, InstitutionRepository institutionRepository, CriterionRepository criterionRepository) {
    this.benefitRepository = benefitRepository;
    this.cityRepository = cityRepository;
    this.institutionRepository = institutionRepository;
    this.criterionRepository = criterionRepository;
  }

  /**
   * Добавляет новое пособие
   * @param benefitAdd объект запроса для добавления пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   */
  @Override
  public void add(BenefitAdd benefitAdd) throws AlreadyExistsException {

    BenefitEntity benefitEntity = BenefitConverter.fromAdd(benefitAdd);

    if (benefitRepository.existsByName(benefitEntity.getName())) {
      throw new AlreadyExistsException(String.format(
          "The benefit %s already exists", benefitEntity.getName()
      ));
    }

    benefitRepository.saveAndFlush(benefitEntity);
  }

  /**
   * Обновляет пособие
   * @param benefitUpdate объект запроса для обновления пособия
   * @throws NotFoundException если пособие с указанными данными не найден
   */
  @Override
  public void update(BenefitUpdate benefitUpdate) throws NotFoundException {

    BenefitEntity benefitEntity = BenefitConverter.fromUpdate(benefitUpdate);

    if (!benefitRepository.existsById(benefitEntity.getId())) {
      throw new NotFoundException(String.format(
          "Benefit with ID %s not found", benefitEntity.getId()
      ));
    }

    benefitRepository.saveAndFlush(benefitEntity);
  }

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idBenefit) throws NotFoundException {

    if (!benefitRepository.existsById(idBenefit)) {
      throw new NotFoundException(String.format(
          "Benefit with ID %s not found", idBenefit
      ));
    }

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

    return BenefitConverter.toInfo(benefitRepository.findById(idBenefit)
                                       .orElseThrow(
                                           () -> new NotFoundException(String.format(
                                               "Benefit with ID %s not found", idBenefit
                                           )))
    );
  }

  /**
   * Возваращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, критериях и учреждениях
   * @return дополнительные данные для пособия
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public BenefitInitData getInitData() throws NotFoundException {

    Set<ObjectShortInfo> cityShortInfoSet = cityRepository.findAll()
        .stream()
        .map(CityConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (cityShortInfoSet.isEmpty()) {
      throw new NotFoundException("Cities not found");
    }

    Set<ObjectShortInfo> criterionShortInfoSet = criterionRepository.findAll()
        .stream()
        .map(CriterionConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (criterionShortInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    Set<ObjectShortInfo> institutionShortInfoSet = institutionRepository.findAll()
        .stream()
        .map(InstitutionConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (institutionShortInfoSet.isEmpty()) {
      throw new NotFoundException("Institutions not found");
    }

    return BenefitInitData
        .builder()
        .shortCitySet(cityShortInfoSet)
        .shortCriterionSet(criterionShortInfoSet)
        .shortInstitutionSet(institutionShortInfoSet)
        .build();
  }

  /**
   * Возвращает множество всех пособий, в том числе без типа критерия
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  @Override
  public Set<BenefitInfo> readAll() throws NotFoundException {

    Set<BenefitInfo> benefitInfoSet = benefitRepository.findAll()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
    if (benefitInfoSet.isEmpty()) {
      throw new NotFoundException("Benefits not found");
    }

    return benefitInfoSet;
  }

  /**
   * Возваращает множество городов учреждения
   * @param idBenefit ID пособия
   * @return множество городов пособия
   * @throws NotFoundException если города пособия не найдены или пособие не найдено
   */
  @Override
  public Set<CityInfo> readCities(BigInteger idBenefit) throws NotFoundException {

    if (!benefitRepository.existsById(idBenefit)) {
      throw new NotFoundException(String.format(
          "Benefit with ID %s not found", idBenefit
      ));
    }

    Set<CityInfo> cityInfoSet = cityRepository.findAllWhereBenefitIdEquals(idBenefit)
        .stream()
        .map(CityConverter::toInfo)
        .collect(Collectors.toSet());
    if (cityInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Cities of benefit with id %s not found", idBenefit
      ));
    }

    return cityInfoSet;
  }

  /**
   * Возваращает множество учреждений учреждения
   * @param idBenefit ID пособия
   * @return множество учреждений пособия
   * @throws NotFoundException если учреждения пособия не найдены или пособие не найдено
   */
  @Override
  public Set<InstitutionInfo> readInstitutions(BigInteger idBenefit) throws NotFoundException {

    if (!benefitRepository.existsById(idBenefit)) {
      throw new NotFoundException(String.format(
          "Benefit with ID %s not found", idBenefit
      ));
    }

    Set<InstitutionInfo> institutionInfoSet = institutionRepository.findAllWhereBenefitIdEquals(idBenefit)
        .stream()
        .map(InstitutionConverter::toInfo)
        .collect(Collectors.toSet());
    if (institutionInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Institutions of benefit with id %s not found", idBenefit
      ));
    }

    return institutionInfoSet;
  }

  /**
   * Возваращает множество критерий учреждения
   * @param idBenefit ID пособия
   * @return множество критерий пособия
   * @throws NotFoundException если критерия пособия не найдены или пособие не найдено
   */
  @Override
  public Set<CriterionInfo> readCriteria(BigInteger idBenefit) throws NotFoundException {

    if (!benefitRepository.existsById(idBenefit)) {
      throw new NotFoundException(String.format(
          "Benefit with ID %s not found", idBenefit
      ));
    }

    Set<CriterionInfo> criterionInfoSet = criterionRepository.findAllWhereBenefitIdEquals(idBenefit)
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException(String.format(
          "Criteria of benefit with id %s not found", idBenefit
      ));
    }

    return criterionInfoSet;
  }
}
