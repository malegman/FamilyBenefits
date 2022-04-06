package com.example.familybenefits.part_res_rest_api.services.interfaces;

import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.city.CitySave;
import com.example.familybenefits.part_res_rest_api.api_model.city.CityInfo;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.NotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса, управляющего объектом "город"
 */
public interface CityService {

  /**
   * Возвращает список городов, в которых есть учреждения и пособия.
   * Фильтр по названию, ID пособия или учреждения.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название города
   * @param idBenefit ID пособия
   * @param idInstitution ID учреждения
   * @return список кратких информаций о городах
   */
  List<ObjectShortInfo> readAllFilter(String name, String idBenefit, String idInstitution);

  /**
   * Создает город по запросу на сохранение
   * @param citySave объект запроса на сохранение города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void create(CitySave citySave)
      throws AlreadyExistsException, InvalidStringException;

  /**
   * Возвращает информацию о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  CityInfo read(String idCity) throws NotFoundException;

  /**
   * Обновляет город по запросу на сохранение
   * @param idCity ID города
   * @param citySave объект запроса на сохранение города
   * @throws NotFoundException если город с указанным ID не найден
   * @throws AlreadyExistsException если город с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void update(String idCity, CitySave citySave)
      throws NotFoundException, AlreadyExistsException, InvalidStringException;

  /**
   * Удаляет город по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  void delete(String idCity) throws NotFoundException;

  /**
   * Возвращает список городов, в которых нет учреждений или пособий
   * @return список кратких информаций о городах
   */
  List<ObjectShortInfo> readAllPartial();

  /**
   * Проверяет существование города по его ID
   * @param idCity ID города, предварительно обработанный
   * @return true, если город найден
   */
  boolean existsById(String idCity);

  /**
   * Возвращает список кратких информаций о городах, в которых есть пособия и учреждения
   * @return список кратких информаций о городах
   */
  List<ObjectShortInfo> readAllFullShort();

  /**
   * Возвращает название города по его ID. Если город не найден, возвращается {@code null}
   * @param idCity ID города
   * @return название города, если город найден, иначе {@code null}
   */
  String readName(String idCity);
}
