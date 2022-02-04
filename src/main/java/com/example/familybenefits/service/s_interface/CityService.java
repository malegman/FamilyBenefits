package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityInitData;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "город"
 */
public interface CityService {

  /**
   * Добавляет город по запросу на добавление
   * @param cityAdd объект запроса на добавление города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   * @throws NotFoundException если пособие города с указанным ID не найдено
   */
  void add(CityAdd cityAdd) throws AlreadyExistsException, NotFoundException;

  /**
   * Обновляет город по запросу на обновление
   * @param cityUpdate объект запроса на обновление города
   * @throws NotFoundException если город с указанными данными не найден
   */
  void update(CityUpdate cityUpdate) throws NotFoundException;

  /**
   * Удаляет город по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  void delete(String idCity) throws NotFoundException;

  /**
   * Возвращает информацию о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  CityInfo read(String idCity) throws NotFoundException;

  /**
   * Возвращает множество городов, в которых есть учреждения и пособия
   * @return множество информаций о городах
   */
  Set<CityInfo> getAll();

  /**
   * Возвращает множество городов, в которых нет учреждений или пособий
   * @return множество информаций о городах
   */
  Set<CityInfo> getAllPartial();

  /**
   * Возвращает дополнительные данные для города.
   * Данные содержат в себе множества кратких информаций о пособиях
   * @return дополнительные данные для города
   */
  CityInitData getInitData();
}
