package com.example.familybenefits.part_res_rest_api.services.interfaces;

import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.DateTimeException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInfo;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInitData;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitSave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;

import java.util.List;

/**
 * Интерфейс сервиса, управляющего объектом "пособие"
 */
public interface BenefitService {

  /**
   * Возвращает список пособий, в которых есть города, учреждения и критерии.
   * Фильтр по названию, ID города, критерия и учреждения.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название пособия
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return список кратких информаций о пособиях
   */
  List<ObjectShortInfo> readAllFilter(String name, String idCity, String idCriterion, String idInstitution);

  /**
   * Создает пособие по запросу на сохранение
   * @param benefitSave объект запроса для сохранения пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если города, учреждения или критерии с указанными ID не найдены
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void create(BenefitSave benefitSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException;

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  BenefitInfo read(String idBenefit) throws NotFoundException;

  /**
   * Обновляет пособие по запросу на сохранение
   * @param idBenefit ID пособия
   * @param benefitSave объект запроса для сохранения пособия
   * @throws NotFoundException если пособие с указанными данными не найдено
   * @throws AlreadyExistsException если пособие с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void update(String idBenefit, BenefitSave benefitSave)
      throws NotFoundException, AlreadyExistsException, InvalidStringException;

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  void delete(String idBenefit) throws NotFoundException;

  /**
   * Возвращает список пособий, в которых нет городов, учреждений или критерий
   * @return список кратких информаций о пособиях
   */
  List<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях
   * @return дополнительные данные для пособия
   */
  BenefitInitData getInitData();

  /**
   * Возвращает подобранные пользователю пособия
   * @param idUser ID пользователя
   * @return список кратких информаций о пособиях
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  List<ObjectShortInfo> readAllOfUser(String idUser)
      throws NotFoundException, DateTimeException;

  /**
   * Проверяет существование пособия по его ID
   * @param idBenefit ID пособия, предварительно обработанный
   * @return true, если пособие найдено
   */
  boolean existsById(String idBenefit);

  /**
   * Возвращает список кратких информаций пособий, в которых есть город, критерий и учреждение
   * @return список кратких информаций пособий
   */
  List<ObjectShortInfo> findAllFullShort();
}
