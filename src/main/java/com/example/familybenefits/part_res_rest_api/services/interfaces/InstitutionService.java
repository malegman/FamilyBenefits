package com.example.familybenefits.part_res_rest_api.services.interfaces;

import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionSave;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInitData;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.NotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса, управляющего объектом "учреждение"
 */
public interface InstitutionService {

  /**
   * Возвращает список учреждений, в которых есть пособия и города.
   * Фильтр по названию, ID города или пособия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название учреждения
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return список кратких информаций об учреждениях
   */
  List<ObjectShortInfo> readAllFilter(String name, String idCity, String idBenefit);

  /**
   * Создает учреждение по запросу на сохранение
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город или пособия с указанными ID не найдены
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void create(InstitutionSave institutionSave)
      throws AlreadyExistsException, NotFoundException, InvalidStringException;

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
   * @throws AlreadyExistsException если учреждение с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  void update(String idInstitution, InstitutionSave institutionSave)
      throws NotFoundException, AlreadyExistsException, InvalidStringException;

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  void delete(String idInstitution) throws NotFoundException;

  /**
   * Возвращает список учреждений, в которых нет пособий
   * @return список кратких информаций об учреждениях
   */
  List<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе список кратких информаций о городах.
   * @return дополнительные данные для учреждения
   */
  InstitutionInitData getInitData();

  /**
   * Проверяет существование учреждения по его ID
   * @param idInstitution ID учреждения, предварительно обработанный
   * @return true, если учреждение найдено
   */
  boolean existsById(String idInstitution);

  /**
   * Возвращает список кратких информаций учреждений, в которых есть пособие и город
   * @return список кратких информаций учреждений
   */
  List<ObjectShortInfo> findAllFullShort();
}
