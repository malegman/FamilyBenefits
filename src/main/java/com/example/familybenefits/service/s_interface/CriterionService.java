package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion.CriterionSave;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.criterion.CriterionInitData;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "критерий"
 */
public interface CriterionService {

  /**
   * Возвращает множество критерий, в которых есть пособия.
   * Фильтр по пособию и типу критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return множество кратких информаций о критериях
   */
  Set<ObjectShortInfo> readAllFilter(String idBenefit, String idCriterionType);

  /**
   * Создает критерий по запросу на сохранение
   * @param criterionSave объект запроса для сохранения критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия данного критерия с указанным ID не найден
   */
  void create(CriterionSave criterionSave) throws AlreadyExistsException, NotFoundException;

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  CriterionInfo read(String idCriterion) throws NotFoundException;

  /**
   * Обновляет данные критерия по запросу на сохранение
   * @param idCriterion ID критерия
   * @param criterionSave объект запроса для сохранения критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   * @throws AlreadyExistsException если критерий с отличным ID и данным названием уже существует
   */
  void update(String idCriterion, CriterionSave criterionSave) throws NotFoundException, AlreadyExistsException;

  /**
   * Удаляет критерий по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  void delete(String idCriterion) throws NotFoundException;

  /**
   * Возвращает множество критерий, в которых нет пособий
   * @return множество кратких информаций о критериях
   */
  Set<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе множество кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   */
  CriterionInitData getInitData();

  /**
   * Возвращает критерии пользователя
   * @param idUser ID пользователя
   * @return множество кратких информаций о критериях
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  Set<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException;
}
