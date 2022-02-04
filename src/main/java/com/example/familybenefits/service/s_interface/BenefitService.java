package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.NotFoundException;

import java.util.Set;

/**
 * Интерфейс сервиса, управляющего объектом "пособие"
 */
public interface BenefitService {

  /**
   * Добавляет новое пособие
   * @param benefitAdd объект запроса для добавления пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдено
   */
  void add(BenefitAdd benefitAdd) throws AlreadyExistsException, NotFoundException;

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
  void delete(String idBenefit) throws NotFoundException;

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  BenefitInfo read(String idBenefit) throws NotFoundException;

  /**
   * Возвращает множество пособий, в которых есть города, учреждения и критерии
   * @return множество информаций о пособиях
   */
  Set<BenefitInfo> getAll();

  /**
   * Возвращает множество пособий, в которых нет городов, учреждений или критерий
   * @return множество информаций о пособиях
   */
  Set<BenefitInfo> getAllPartial();

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для пособия
   */
  BenefitInitData getInitData();
}
