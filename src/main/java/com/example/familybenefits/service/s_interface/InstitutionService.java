package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.institution.InstitutionAdd;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.api_model.institution.InstitutionUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "учреждение"
 */
public interface InstitutionService {

  /**
   * Добавляет учреждение по запросу на добавление
   * @param institutionAdd объект запроса на добавление учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город учреждения с указанным ID не найден
   */
  void add(InstitutionAdd institutionAdd) throws AlreadyExistsException, NotFoundException;

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
  void delete(String idInstitution) throws NotFoundException;

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  InstitutionInfo read(String idInstitution) throws NotFoundException;

  /**
   * Возвращает множество учреждений, в которых есть пособия
   * @return множество информаций об учреждениях
   */
  Set<InstitutionInfo> getAll();

  /**
   * Возвращает множество учреждений, в которых нет пособий
   * @return множество информаций об учреждениях
   */
  Set<InstitutionInfo> getAllPartial();

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе множество кратких информаций о городах.
   * @return дополнительные данные для учреждения
   */
  InstitutionInitData getInitData();
}
