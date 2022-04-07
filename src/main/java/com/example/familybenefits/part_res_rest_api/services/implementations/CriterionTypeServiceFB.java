package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.CriterionTypeEntity;
import com.example.familybenefits.dto.repositories.CriterionTypeRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion_type.CriterionTypeSave;
import com.example.familybenefits.part_res_rest_api.converters.CriterionTypeDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionTypeService;
import com.example.familybenefits.security.DBSecuritySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "тип критерия"
 */
@Service
public class CriterionTypeServiceFB implements CriterionTypeService {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion_type"
   */
  private final CriterionTypeRepository criterionTypeRepository;

  /**
   * Конструктор для инициализации интерфейсов репозитория и сервиса
   * @param criterionTypeRepository репозиторий, работающий с моделью таблицы "criterion_type"
   */
  @Autowired
  public CriterionTypeServiceFB(CriterionTypeRepository criterionTypeRepository) {
    this.criterionTypeRepository = criterionTypeRepository;
  }

  /**
   * Возвращает список типов критерия, в которых есть критерии
   * Фильтр по названию или ID критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название типа критерия
   * @param idCriterion ID критерия
   * @return список кратких информаций о типах критерий
   */
  @Override
  public List<ObjectShortInfo> readAllFilter(String name, String idCriterion) {

    String preparedName = DBSecuritySupport.preparePostgreSQLString(name);
    String preparedIdCriterion = DBSecuritySupport.preparePostgreSQLString(idCriterion);

    return criterionTypeRepository.findAllFilter(preparedName, preparedIdCriterion)
        .stream()
        .map(CriterionTypeDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Создает тип критерия по запросу на сохранение
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @throws AlreadyExistsException если тип критерия с указанным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(CriterionTypeSave criterionTypeSave) throws AlreadyExistsException, InvalidStringException {

    // Проверка отсутствия типа критерия по его названию
    DBSecuritySupport.checkAbsenceByUniqStr(criterionTypeRepository::existsByName, criterionTypeSave.getName());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionTypeEntity criterionTypeEntityFromSave = CriterionTypeDBConverter
        .fromSave(null, criterionTypeSave, DBSecuritySupport::preparePostgreSQLString);

    criterionTypeRepository.saveAndFlush(criterionTypeEntityFromSave);
  }

  /**
   * Возвращает информацию о типе критерия по его ID
   * @param idCriterionType ID типа критерия
   * @return информация о типе критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @Override
  public CriterionTypeInfo read(String idCriterionType) throws NotFoundException {

    // Получение типа критерия по его ID, если тип критерия существует
    String prepareIdCriterionType = DBSecuritySupport.preparePostgreSQLString(idCriterionType);
    CriterionTypeEntity criterionTypeEntityFromRequest = criterionTypeRepository.findById(prepareIdCriterionType).orElseThrow(
        () -> new NotFoundException(String.format("Criterion type with ID \"%s\" not found", idCriterionType)));

    return CriterionTypeDBConverter.toInfo(criterionTypeEntityFromRequest);
  }

  /**
   * Обновляет тип критерия по запросу на сохранение
   * @param idCriterionType ID типа критерия
   * @param criterionTypeSave объект запроса для сохранения типа критерия
   * @throws NotFoundException если тип критерия с указанными данными не найден
   * @throws AlreadyExistsException если тип критерия с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idCriterionType, CriterionTypeSave criterionTypeSave) throws NotFoundException, AlreadyExistsException, InvalidStringException {

    // Проверка существования типа критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionTypeRepository::existsById, idCriterionType);
    // Проверка отсутствия критерия с отличным от данного ID и данным названием
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        criterionTypeRepository::existsByIdIsNotAndName, idCriterionType, criterionTypeSave.getName());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionTypeEntity criterionTypeEntityFromSave = CriterionTypeDBConverter
        .fromSave(idCriterionType, criterionTypeSave, DBSecuritySupport::preparePostgreSQLString);

    criterionTypeEntityFromSave.setId(idCriterionType);

    criterionTypeRepository.saveAndFlush(criterionTypeEntityFromSave);
  }

  /**
   * Удаляет тип критерия по его ID
   * @param idCriterionType ID типа критерия
   * @throws NotFoundException если тип критерия с указанным ID не найден
   */
  @Override
  public void delete(String idCriterionType) throws NotFoundException {

    // Проверка существования типа критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionTypeRepository::existsById, idCriterionType);

    criterionTypeRepository.deleteById(idCriterionType);
  }

  /**
   * Возвращает список типов критерия, в которых нет критерий
   * @return список кратких информаций о типах критерий
   */
  @Override
  public List<ObjectShortInfo> readAllPartial() {

    return criterionTypeRepository.findAllPartial()
        .stream()
        .map(CriterionTypeDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Проверяет существование типа критерия по его ID
   * @param idUser ID типа критерия, предварительно обработанный
   * @return true, если тип критерия найден
   */
  @Override
  public boolean existsById(String idUser) {

    return criterionTypeRepository.existsById(idUser);
  }

  /**
   * Возвращает список кратких информаций типов критерия, в которых есть критерии
   * @return список кратких информаций типов критерия
   */
  @Override
  public List<ObjectShortInfo> readAllFullShort() {

    return criterionTypeRepository.findAllFilter(null, null)
        .stream()
        .map(CriterionTypeDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Возвращает название типа критерия по ID критерия. Если критерий не найден, возвращается {@code null}
   * @param idCriterion ID города
   * @return название города, если город найден, иначе {@code null}
   */
  @Override
  public String readNameByIdCriterion(String idCriterion) {

    // Получение типа критерия по ID критерия, если критерий существует
    String preparedIdCriterion = DBSecuritySupport.preparePostgreSQLString(idCriterion);
    List<CriterionTypeEntity> criterionTypeEntityList = criterionTypeRepository.findAllFilter(null, preparedIdCriterion);

    if (criterionTypeEntityList.isEmpty()) {
      return null;
    } else {
      return criterionTypeEntityList.get(0).getName();
    }
  }
}
