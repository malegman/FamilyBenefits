package com.example.familybenefits.service;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "критерий"
 */
public interface CriterionService {

  /**
   * Добавляет новый критерий
   * @param criterionAdd объект запроса для добавления критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   */
  void add(CriterionAdd criterionAdd) throws AlreadyExistsException;

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   */
  void update(CriterionUpdate criterionUpdate) throws NotFoundException;

  /**
   * Удаляет критерия по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  void delete(BigInteger idCriterion) throws NotFoundException;

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  CriterionInfo read(BigInteger idCriterion) throws NotFoundException;

  /**
   * Возвращает множество всех полных критерий - с типом критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  Set<CriterionInfo> readAllFull() throws NotFoundException;

  /**
   * Возвращает множество всех неполных критерий - без типа критерия
   * @return множество информаций о критериях
   * @throws NotFoundException если критерии не найдены
   */
  Set<CriterionInfo> readAllPartial() throws NotFoundException;

  /**
   * Возваращает дополнительные данные для критерия.
   * Данные содержат в себе множетсво кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   * @throws NotFoundException если данные не найдены
   */
  CriterionInitData getInitData() throws NotFoundException;

  /**
   * Возвращает информацию о типе критерия критерия.
   * @param idCriterion ID критерия
   * @return информация о типе критерия критерия
   * @throws NotFoundException если тип критерия не найден или критерий не найден
   */
  CriterionTypeInfo readCriterionType(BigInteger idCriterion) throws NotFoundException;
}
