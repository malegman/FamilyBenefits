package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.benefit.BenefitSave;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.DateTimeException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "пособие"
 */
public interface BenefitService {

  /**
   * Возвращает множество пособий, в которых есть города, учреждения и критерии.
   * Фильтр по городу, критерию и учреждению.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return множество кратких информаций о пособиях
   */
  Set<ObjectShortInfo> readAllFilter(String idCity, String idCriterion, String idInstitution);

  /**
   * Создает пособие по запросу на сохранение
   * @param benefitSave объект запроса для сохранения пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдено
   */
  void create(BenefitSave benefitSave) throws AlreadyExistsException, NotFoundException;

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
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  void update(String idBenefit, BenefitSave benefitSave) throws NotFoundException;

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найдено
   */
  void delete(String idBenefit) throws NotFoundException;

  /**
   * Возвращает множество пособий, в которых нет городов, учреждений или критерий
   * @return множество кратких информаций о пособиях
   */
  Set<ObjectShortInfo> readAllPartial();

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях
   * @return дополнительные данные для пособия
   */
  BenefitInitData getInitData();

  /**
   * Возвращает подобранные пользователю пособия
   * @param idUser ID пользователя
   * @return множество кратких информаций о пособиях
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  Set<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException, DateTimeException;
}
