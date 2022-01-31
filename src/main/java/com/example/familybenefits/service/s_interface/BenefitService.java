package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.api_model.benefit.BenefitAdd;
import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitUpdate;
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
  void delete(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  BenefitInfo read(BigInteger idBenefit) throws NotFoundException;

  /**
   * Возвращает множество всех полных пособий - с городом, учреждением и критерием
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  Set<BenefitInfo> readAllFull() throws NotFoundException;

  /**
   * Возвращает множество всех неполных пособий - без города, учреждения или критерия
   * @return множество информаций о пособиях
   * @throws NotFoundException если пособия не найдены
   */
  Set<BenefitInfo> readAllPartial() throws NotFoundException;

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для пособия
   * @throws NotFoundException если данные не найдены
   */
  BenefitInitData getInitData() throws NotFoundException;
}
