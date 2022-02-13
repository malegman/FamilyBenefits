package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.api_model.institution.InstitutionSave;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInitData;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "учреждение"
 */
public interface InstitutionService {

  /**
   * Возвращает множество учреждений, в которых есть пособия.
   * Фильтр по городу и пособию.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return множество кратких информаций об учреждениях
   */
  Set<ObjectShortInfo> readAllFilter(String idCity, String idBenefit);

  /**
   * Создает учреждение по запросу на сохранение
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город или пособия с указанными ID не найдены
   */
  void create(InstitutionSave institutionSave) throws AlreadyExistsException, NotFoundException;

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  InstitutionInfo read(String idInstitution) throws NotFoundException;

  /**
   * Обновляет учреждение по запросу на сохранение
   * @param idInstitution ID учреждения
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws NotFoundException если учреждение, город или пособия с указанными ID не найдены
   */
  void update(String idInstitution, InstitutionSave institutionSave) throws NotFoundException;

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  void delete(String idInstitution) throws NotFoundException;

  /**
   * Возвращает множество учреждений, в которых нет пособий
   * @return множество кратких информаций об учреждениях
   */
  Set<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе множество кратких информаций о городах.
   * @return дополнительные данные для учреждения
   */
  InstitutionInitData getInitData();
}
