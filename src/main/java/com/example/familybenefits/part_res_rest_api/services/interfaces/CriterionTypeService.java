package com.example.familybenefits.part_res_rest_api.services.interfaces;

import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.NotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса, управляющего объектом "тип критерия"
 */
public interface CriterionTypeService {

  /**
   * Возвращает список типов критерия, в которых есть критерии
   * Фильтр по названию или ID критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название типа критерия
   * @param idCriterion ID критерия
   * @return список кратких информаций о типах критерий
   */
  List<ObjectShortInfo> readAllFilter(String name, String idCriterion);

  /**
   * Создает тип критерия по запросу на сохранение
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void create(CriterionTypeSave criterionTypeSave) throws AlreadyExistsException, InvalidStringException;

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
   * @throws AlreadyExistsException если тип критерия с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void update(String idCriterionType, CriterionTypeSave criterionTypeSave)
      throws NotFoundException, AlreadyExistsException, InvalidStringException;

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  void delete(String idCriterionType) throws NotFoundException;

  /**
   * Возвращает список типов критерия, в которых нет критерий
   * @return список кратких информаций о типах критерий
   */
  List<ObjectShortInfo> readAllPartial();

  /**
   * Проверяет существование типа критерия по его ID
   * @param idUser ID типа критерия, предварительно обработанный
   * @return true, если тип критерия найден
   */
  boolean existsById(String idUser);

  /**
   * Возвращает список кратких информаций типов критерия, в которых есть критерии
   * @return список кратких информаций типов критерия
   */
  List<ObjectShortInfo> readAllFullShort();

  /**
   * Возвращает название типа критерия по ID критерия. Если критерий не найден, возвращается {@code null}
   * @param idCriterion ID города
   * @return название города, если город найден, иначе {@code null}
   */
  String readNameByIdCriterion(String idCriterion);
}
