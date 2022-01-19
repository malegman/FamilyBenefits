package com.example.familybenefits.service;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.criterion.CriterionInfo;
import com.example.familybenefits.api_model.institution.InstitutionInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.math.BigInteger;
import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "пособие"
 */
public interface BenefitService {

  /**
   * Добавляет новое пособие
   * @param benefitAdd объект запроса для добавления пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   */
  void add(BenefitAdd benefitAdd) throws AlreadyExistsException;

  /**
   * Обновляет пособие
   * @param benefitUpdate объект запроса для обновления пособия
   * @throws NotFoundException если пособие с указанными данными не найден
   */
  void update(BenefitUpdate benefitUpdate) throws NotFoundException;

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  void delete(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  BenefitInfo read(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возваращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, критериях и учреждениях
   * @return дополнительные данные для пособия
   * @throws NotFoundException если данные не найдены
   */
  BenefitInitData getInitData() throws NotFoundException;

  /**
   * Возвращает множество всех пособий, в том числе без типа критерия
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  Set<BenefitInfo> readAll() throws NotFoundException;

  /**
   * Возваращает множество городов учреждения
   * @param idBenefit ID пособия
   * @return множество городов пособия
   * @throws NotFoundException если города пособия не найдены или пособие не найдено
   */
  Set<CityInfo> readCities(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возваращает множество учреждений учреждения
   * @param idBenefit ID пособия
   * @return множество учреждений пособия
   * @throws NotFoundException если учреждения пособия не найдены или пособие не найдено
   */
  Set<InstitutionInfo> readInstitutions(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возваращает множество критерий учреждения
   * @param idBenefit ID пособия
   * @return множество критерий пособия
   * @throws NotFoundException если критерия пособия не найдены или пособие не найдено
   */
  Set<CriterionInfo> readCriteria(BigInteger idBenefit) throws NotFoundException;
}
