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
   * Добавляет учреждение по запросу на добавление
   * @param institutionAdd объект запроса на добавление учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   */
  void add(InstitutionAdd institutionAdd) throws AlreadyExistsException;

  /**
   * Обновляет учреждение по запросу на обновление
   * @param institutionUpdate объект запроса на обновление учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  void update(InstitutionUpdate institutionUpdate) throws NotFoundException;

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  void delete(BigInteger idInstitution) throws NotFoundException;

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  InstitutionInfo read(BigInteger idInstitution) throws NotFoundException;

  /**
   * Возваращает дополнительные данные для учреждения
   * @return дополнительные данные для учреждения
   * @throws NotFoundException если данные не найдены
   */
  InstitutionInitData getInitData() throws NotFoundException;

  /**
   * Возвращает информацию о городе учреждения
   * @param idInstitution ID учреждения
   * @return информация о городе учреждения
   * @throws NotFoundException если город не найден или учреждение не найдено
   */
  CityInfo readCity(BigInteger idInstitution) throws NotFoundException;

  /**
   * Возваращает множество пособий учреждения
   * @param idInstitution ID учреждения
   * @return множество пособий учреждений
   * @throws NotFoundException если пособия учреждения не найдены или учреждение не найдено
   */
  Set<BenefitInfo> readBenefits(BigInteger idInstitution) throws NotFoundException;
}
