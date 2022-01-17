package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "город"
 */
public interface CityService {

  /**
   * Добавление города по запросу на добавление
   * @param cityAdd объект запроса на добавление города
   * @throws AlreadyExistsException если город с указанным названием уже существует
   */
  void add(CityAdd cityAdd) throws AlreadyExistsException;

  /**
   * Обновление города по запросу на обновление
   * @param cityUpdate объект запроса на обновление города
   * @throws NotFoundException если город с указанными данными не найден
   */
  void update(CityUpdate cityUpdate) throws NotFoundException;

  /**
   * Удаление города по его ID
   * @param idCity ID города
   * @throws NotFoundException если город с указанным ID не найден
   */
  void delete(BigInteger idCity) throws NotFoundException;

  /**
   * Получение информации о городе по его ID
   * @param idCity ID города
   * @return информация о городе
   * @throws NotFoundException если город с указанным ID не найден
   */
  CityInfo read(BigInteger idCity) throws NotFoundException;

  /**
   * Получение множества всех существующих городов
   * @return множество информаций о городах
   * @throws NotFoundException если города не найдены
   */
  Set<CityInfo> readAll() throws NotFoundException;

  /**
   * Получение множества всех учреждений города
   * @param idCity ID города
   * @return множество информаций о городах
   * @throws NotFoundException если учреждения не найдены или город с указынным ID не найден
   */
  Set<InstitutionInfo> readInstitutions(BigInteger idCity) throws NotFoundException;

  /**
   * Получение множества всех пособий города
   * @param idCity ID города
   * @return множество информаций о городах
   * @throws NotFoundException если пособия не найдены или город с указынным ID не найден
   */
  Set<BenefitInfo> readBenefits(BigInteger idCity) throws NotFoundException;
}
