package com.example.familybenefits.part_res_rest_api.services.interfaces;

import com.example.familybenefits.dto.entities.CriterionEntity;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionSave;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInitData;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.NotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса, управляющего объектом "критерий"
 */
public interface CriterionService {

  /**
   * Возвращает список критерий, в которых есть пособия.
   * Фильтр по названию, ID пособия или типа критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название критерия
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return список кратких информаций о критериях
   */
  List<ObjectShortInfo> readAllFilter(String name, String idBenefit, String idCriterionType);

  /**
   * Создает критерий по запросу на сохранение
   * @param criterionSave объект запроса для сохранения критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия данного критерия с указанным ID не найден
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void create(CriterionSave criterionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException;

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
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void update(String idCriterion, CriterionSave criterionSave)
      throws NotFoundException, AlreadyExistsException, InvalidStringException;

  /**
   * Удаляет критерий по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  void delete(String idCriterion) throws NotFoundException;

  /**
   * Возвращает список критерий, в которых нет пособий
   * @return список кратких информаций о критериях
   */
  List<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе список кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   */
  CriterionInitData getInitData();

  /**
   * Возвращает критерии пользователя
   * @param idUser ID пользователя
   * @return список кратких информаций о критериях
   * @throws NotFoundException если пользователь не найден
   */
  List<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException;

  /**
   * Возвращает критерии пособия
   * @param idBenefit ID пособия
   * @return список кратких информаций о критериях
   */
  List<ObjectShortInfo> readAllOfBenefit(String idBenefit);

  /**
   * Проверяет существование критерия по его ID
   * @param idCity ID критерия, предварительно обработанный
   * @return true, если критерий найден
   */
  boolean existsById(String idCity);

  /**
   * Возвращает список информаций о критериях, в которых есть пособия
   * @return список информаций о критериях
   */
  List<CriterionInfo> readAllFull();
}
