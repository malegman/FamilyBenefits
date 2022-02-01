package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.criterion.CriterionAdd;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.api_model.criterion.CriterionUpdate;
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
   * @throws NotFoundException если тип критерия критерия с указанным ID не найден
   */
  void add(CriterionAdd criterionAdd) throws AlreadyExistsException, NotFoundException;

  /**
   * Обновляет данные критерия
   * @param criterionUpdate объект запроса для обновления критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   */
  void update(CriterionUpdate criterionUpdate) throws NotFoundException;

  /**
   * Удаляет критерий по его ID
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
   * Возвращает множество критерий, в которых есть пособия
   * @return множество информаций о критериях
   */
  Set<CriterionInfo> getAll();

  /**
   * Возвращает множество критерий, в которых нет пособий
   * @return множество информаций о критериях
   */
  Set<CriterionInfo> getAllPartial();

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе множество кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   */
  CriterionInitData getInitData();
}
