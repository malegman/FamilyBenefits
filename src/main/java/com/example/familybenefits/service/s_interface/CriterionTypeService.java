package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "тип критерия"
 */
public interface CriterionTypeService {

  /**
   * Возвращает множество типов критерия, в которых есть критерии
   * @return множество кратких информаций о типах критерий
   */
  Set<ObjectShortInfo> readAll();

  /**
   * Создает тип критерия по запросу на сохранение
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   */
  void create(CriterionTypeSave criterionTypeSave) throws AlreadyExistsException;

  /**
   * Возвращает информацию о типе критерия по его ID
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  CriterionTypeInfo read(String idCriterionType) throws NotFoundException;

  /**
   * Обновляет тип критерия по запросу на сохранение
   * @param idCriterionType ID типа критерия
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   */
  void update(String idCriterionType, CriterionTypeSave criterionTypeSave) throws NotFoundException;

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  void delete(String idCriterionType) throws NotFoundException;

  /**
   * Возвращает множество типов критерия, в которых нет критерий
   * @return множество кратких информаций о типах критерий
   */
  Set<ObjectShortInfo> readAllPartial();
}
