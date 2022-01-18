package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "учреждение"
 */
public interface InstitutionService {

  /**
   * Добавление учреждения по запросу на добавление
   * @param institutionAdd объект запроса на добавление учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   */
  void add(InstitutionAdd institutionAdd) throws AlreadyExistsException;

  /**
   * Обновление учреждения по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  void update(InstitutionUpdate institutionUpdate) throws NotFoundException;

  /**
   * Удаление учреждения по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  void delete(BigInteger idInstitution) throws NotFoundException;

  /**
   * Получение информации об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  InstitutionInfo read(BigInteger idInstitution) throws NotFoundException;

  /**
   * Получение дополнительных данных для учреждения
   * @return дополнительные данные для учреждения
   * @throws NotFoundException если данные не найдены
   */
  InstitutionInitData getInitData() throws NotFoundException;

  /**
   * Получение информации о городе учреждения
   * @param idInstitution ID учреждения
   * @return информация о городе учреждения
   * @throws NotFoundException если город не найден или учреждение не найдено
   */
  CityInfo readCity(BigInteger idInstitution) throws NotFoundException;

  /**
   * Получение множества пособий учреждения
   * @param idInstitution ID учреждения
   * @return множество пособий учреждений
   * @throws NotFoundException если пособия учреждения не найдены или учреждение не найдено
   */
  Set<BenefitInfo> readBenefits(BigInteger idInstitution) throws NotFoundException;
}
