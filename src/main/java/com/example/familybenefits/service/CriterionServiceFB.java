package com.example.familybenefits.service;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.CriterionTypeConverter;
import com.example.familybenefits.dao.entity.CriterionEntity;
import com.example.familybenefits.dao.repository.CriterionRepository;
import com.example.familybenefits.dao.repository.CriterionTypeRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "критерий"
 */
@Service
public class CriterionServiceFB implements CriterionService {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "criterion_type"
   */
  private final CriterionTypeRepository criterionTypeRepository;

  /**
   * Конструктор для инициализации интерфейсов репозиториев
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param criterionTypeRepository репозиторий, работающий с моделью таблицы "criterion_type"
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository, CriterionTypeRepository criterionTypeRepository) {
    this.criterionRepository = criterionRepository;
    this.criterionTypeRepository = criterionTypeRepository;
  }

  /**
   * Добавляет новый критерий
   * @param criterionAdd объект запроса для добавления критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   */
  @Override
  public void add(CriterionAdd criterionAdd) throws AlreadyExistsException {

    ServiceHelper.checkAbsenceObjectByUniqStrElseThrow(
        criterionTypeRepository::existsByName, criterionAdd.getName(), "The criterion with name %s already exists");

    criterionRepository.saveAndFlush(CriterionConverter.fromAdd(criterionAdd));
  }

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   */
  @Override
  public void update(CriterionUpdate criterionUpdate) throws NotFoundException {

    ServiceHelper.checkExistenceObjectByIdElseThrow(
        criterionTypeRepository::existsById, criterionUpdate.getId(), "Criterion with ID %s not found");

    criterionRepository.saveAndFlush(CriterionConverter.fromUpdate(criterionUpdate));
  }

  /**
   * Удаляет критерийпо его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idCriterion) throws NotFoundException {

    ServiceHelper.checkExistenceObjectByIdElseThrow(
        criterionTypeRepository::existsById, idCriterion, "Criterion with ID %s not found");

    criterionRepository.deleteById(idCriterion);
  }

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public CriterionInfo read(BigInteger idCriterion) throws NotFoundException {

    ServiceHelper.checkExistenceObjectByIdElseThrow(
        criterionTypeRepository::existsById, idCriterion, "Criterion with ID %s not found");

    return CriterionConverter.toInfo(criterionRepository.getById(idCriterion));
  }

  /**
   * Возвращает множество всех полных критерий - с типом критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  @Override
  public Set<CriterionInfo> readAllFull() throws NotFoundException {

    Set<CriterionInfo> criterionInfoSet = criterionRepository.findAllByCriterionTypeIsNotNull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    return criterionInfoSet;
  }

  /**
   * Возвращает множество всех неполных критерий - без типа критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  @Override
  public Set<CriterionInfo> readAllPartial() throws NotFoundException {

    Set<CriterionInfo> criterionInfoSet = criterionRepository.findAllByCriterionTypeIsNull()
        .stream()
        .map(CriterionConverter::toInfo)
        .collect(Collectors.toSet());
    if (criterionInfoSet.isEmpty()) {
      throw new NotFoundException("Criteria not found");
    }

    return criterionInfoSet;
  }

  /**
   * Возваращает дополнительные данные для критерия.
   * Данные содержат в себе множетсво кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   * @throws NotFoundException если данные не найдены
   */
  @Override
  public CriterionInitData getInitData() throws NotFoundException {

    Set<ObjectShortInfo> criterionTypeShortInfoSet = criterionTypeRepository.findAll()
        .stream()
        .map(CriterionTypeConverter::toShortInfo)
        .collect(Collectors.toSet());
    if (criterionTypeShortInfoSet.isEmpty()) {
      throw new NotFoundException("Criterion types not found");
    }

    return CriterionInitData
        .builder()
        .shortCriterionTypeSet(criterionTypeShortInfoSet)
        .build();
  }

  /**
   * Возвращает информацию о типе критерия критерия.
   * @param idCriterion ID критерия
   * @return информация о типе критерия критерия
   * @throws NotFoundException если тип критерия не найден или критерий не найден
   */
  @Override
  public CriterionTypeInfo readCriterionType(BigInteger idCriterion) throws NotFoundException {

    ServiceHelper.checkExistenceObjectByIdElseThrow(
        criterionTypeRepository::existsById, idCriterion, "Criterion with ID %s not found");

    CriterionEntity criterionEntity = criterionRepository.getById(idCriterion);

    if (criterionEntity.getCriterionType() == null) {
      throw new NotFoundException(String.format("Criterion type of criterion with ID %s not found", idCriterion));
    }

    return CriterionTypeConverter.toInfo(criterionEntity.getCriterionType());
  }
}
