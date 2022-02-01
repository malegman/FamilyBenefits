package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.criterion_type.CriterionTypeAdd;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.api_model.criterion_type.CriterionTypeUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "тип критерия"
 */
public interface CriterionTypeService {

  /**
   * Добавляет новый тип критерия
   * @param criterionTypeAdd объект запроса для добавления типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   */
  void add(CriterionTypeAdd criterionTypeAdd) throws AlreadyExistsException;

  /**
   * Обновляет данные типа критерия
   * @param criterionTypeUpdate объект запроса для обновления типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   */
  void update(CriterionTypeUpdate criterionTypeUpdate) throws NotFoundException;

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  void delete(BigInteger idCriterionType) throws NotFoundException;

  /**
   * Возвращает информацию о типе критерия по его ID
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  CriterionTypeInfo read(BigInteger idCriterionType) throws NotFoundException;

  /**
   * Возвращает множество типов критерия, в которых есть критерии
   * @return множество информаций о типах критерий
   */
  Set<CriterionTypeInfo> getAll();

  /**
   * Возвращает множество типов критерия, в которых нет критерий
   * @return множество информаций о типах критерий
   */
  Set<CriterionTypeInfo> getAllPartial();
}
